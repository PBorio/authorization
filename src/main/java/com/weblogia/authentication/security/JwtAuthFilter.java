package com.weblogia.authentication.security;

import com.weblogia.authentication.exceptions.CompanyNotInformedException;
import com.weblogia.authentication.model.Company;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.repositories.CompanyRepository;
import com.weblogia.authentication.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository,
                         CompanyRepository companyRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            if(jwtService.validateToken(token, username)){
                UserDetails userDetails = loadUserDetails(token, username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request, response);
    }

    private UserDetails loadUserDetails(String token, String username) {
        Long companyId = jwtService.getCompanyFromToken(token);

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("could not find user..!!");
        }

        if (user.isSysAdmin()) {
            Optional<Company> oCompany = companyRepository.findById(companyId);

            if (oCompany.isEmpty()){
                throw new CompanyNotInformedException(String.format("No company with id %s found", companyId));
            }
            user.setCompany(oCompany.get());
        }

        return new CustomUserDetails(user);
    }
}
