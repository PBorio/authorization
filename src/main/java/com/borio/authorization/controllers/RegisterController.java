package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.UserDto;
import com.borio.authorization.controllers.forms.UserForm;
import com.borio.authorization.domain.User;
import com.borio.authorization.repositories.UserRepository;
import com.borio.authorization.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class RegisterController {


    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService){
        this.registerService = registerService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserForm userForm, UriComponentsBuilder uriBuilder) {
        User createdUser = this.registerService.save(userForm.convert());
        UserDto userDto = new UserDto(createdUser.getId(),createdUser.getEmail());
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userDto.getId()).toUri();

        this.registerService.sendMail(createdUser);

        return ResponseEntity.created(uri).body(userDto);
    }

}
