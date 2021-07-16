package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.UserDto;
import com.borio.authorization.controllers.forms.UserForm;
import com.borio.authorization.domain.User;
import com.borio.authorization.domain.events.OnRegistrationCompleteEvent;
import com.borio.authorization.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Date;

@RestController
public class RegisterController {


    private final RegisterService registerService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public RegisterController(RegisterService registerService, ApplicationEventPublisher applicationEventPublisher){
        this.registerService = registerService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserForm userForm,
                                            HttpServletRequest request,
                                            UriComponentsBuilder uriComponentsBuilder) {

        User createdUser = this.registerService.save(userForm.convertToNewUser());
        UserDto userDto = new UserDto(createdUser.getId(),createdUser.getEmail());

        URI uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(userDto.getId()).toUri();
        String appUrl = request.getContextPath();
        this.applicationEventPublisher.publishEvent(
                   new OnRegistrationCompleteEvent(createdUser, request.getLocale(), appUrl, new Date()));

        return ResponseEntity.created(uri).body(userDto);

    }

    @GetMapping("/register/complete")
    public ResponseEntity<UserDto> registerConfirmation(@RequestParam String token) {
        User user = this.registerService.validateToken(token);
        return ResponseEntity.ok().body(new UserDto(user.getId(), user.getEmail()));
    }

}
