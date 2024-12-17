package com.weblogia.authentication.security;

import com.weblogia.authentication.controller.records.JwtResponseDTO;
import com.weblogia.authentication.exceptions.CompanyNotInformedException;
import com.weblogia.authentication.exceptions.UserNotSysAdminException;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.model.UserRole;
import com.weblogia.authentication.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Array;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    @Autowired
    UserRepository userRepository;

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Long getCompanyFromToken(String token) {
        return extractClaim(token, claims -> claims.get("company", Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String loginUserName) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(loginUserName) && !isTokenExpired(token));
    }



    public JwtResponseDTO generateToken(String username){
        User user = userRepository.findByUsername(username);

        if (user.getCompany() == null) {
            throw new CompanyNotInformedException("No Company is informed on the login");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("company", user.getCompany().getId());
        String token = createToken(claims, username);

        List<String> roles = new ArrayList<>();
        for (UserRole role : user.getRoles()){
            roles.add(role.getName());
        }

        return new JwtResponseDTO(token, roles);
    }

    public JwtResponseDTO generateTokenForSysAdmin(String username, Long companyId) {

        User user = userRepository.findByUsername(username);
        if (!user.isSysAdmin()){
            throw new UserNotSysAdminException("User is no System Administrator");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("company", companyId);
        String token = createToken(claims, username);

        List<String> roles = new ArrayList<>();
        for (UserRole role : user.getRoles()){
            roles.add(role.getName());
        }

        return new JwtResponseDTO(token, roles);
    }



    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
