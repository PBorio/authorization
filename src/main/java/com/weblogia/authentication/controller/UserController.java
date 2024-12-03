package com.weblogia.authentication.controller;

import com.weblogia.authentication.controller.records.ChangePassDto;
import com.weblogia.authentication.controller.records.RegisterUserDto;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.repositories.UserRepository;
import com.weblogia.authentication.services.RegisterUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final RegisterUserService userService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, RegisterUserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<String> registerBasicUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        try {
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (loggedUser == null){
                return ResponseEntity.status(401).build();
            }
            userService.registerBasicUser(new RegisterUserDto(registerUserDto.username(), registerUserDto.password(), loggedUser.getCompany().getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed: " + e.getMessage());
        }
    }

     // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/change-pass/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePassDto changePassDto) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if (!authenticate(existingUser.getUsername(), changePassDto.currentPassword()))
                return ResponseEntity.notFound().build();

            existingUser.updatePassord(passwordEncoder.encode(changePassDto.newPassword()));

            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok("Senha alterada");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        return authentication.isAuthenticated();
    }
}
