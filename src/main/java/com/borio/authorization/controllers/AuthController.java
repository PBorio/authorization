package com.borio.authorization.controllers;

import com.borio.authorization.config.security.TokenService;
import com.borio.authorization.controllers.dtos.TokenDto;
import com.borio.authorization.controllers.forms.UserForm;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
@Hidden
public class AuthController {

    /* Spring won´t inject this component, so we have to config it manually
       that was implemented at config.security.SecurityConfig
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody @Valid UserForm userForm) {

        UsernamePasswordAuthenticationToken loginInfo = userForm.convertToAuthToken();

        try {
            Authentication authentication = authenticationManager.authenticate(loginInfo);
            String token = tokenService.generate(authentication);
            return ResponseEntity.ok("Bearer "+token);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }
}
