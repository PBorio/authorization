package com.weblogia.authentication.controller;

import com.weblogia.authentication.controller.records.AuthRequestDTO;
import com.weblogia.authentication.controller.records.JwtResponseDTO;
import com.weblogia.authentication.controller.records.RegisterUserAdminAndCompanyDTO;
import com.weblogia.authentication.security.JwtService;
import com.weblogia.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.username(), authRequestDTO.password())
        );

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequestDTO.username());
            return new JwtResponseDTO(token);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO) {
        try {
            userService.registerUserAdmin(registerUserAdminAndCompanyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerBasicUser(@RequestBody RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO) {
        try {
            userService.registerBasicUser(registerUserAdminAndCompanyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/sys-admin/register")
    public ResponseEntity<String> registerUserSysAdmin(@RequestBody AuthRequestDTO registerRequestDTO) {
        try {
            userService.registerUserSysAdmin(registerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed: " + e.getMessage());
        }
    }
}
