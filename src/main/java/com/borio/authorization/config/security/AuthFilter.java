package com.borio.authorization.config.security;

import com.borio.authorization.domain.User;
import com.borio.authorization.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    private UserRepository userRepository;

    public AuthFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = getToken(request);
        boolean isValid = tokenService.validate(token);

        if (isValid) {
            authenticate(token);
        }

        filterChain.doFilter(request,response);
    }

    private void authenticate(String token) {
        Long userId = tokenService.getUsuario(token);
        Optional<User> oUser = userRepository.findById(userId);

        if (oUser.isEmpty()) {
            return;
        }
        User user = oUser.get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7, token.length());
    }
}
