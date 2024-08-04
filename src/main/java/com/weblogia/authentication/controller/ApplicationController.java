package com.weblogia.authentication.controller;


import com.weblogia.authentication.model.Application;
import com.weblogia.authentication.model.Company;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.model.ApplicationPermission;
import com.weblogia.authentication.repositories.ApplicationPermissionRepository;
import com.weblogia.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {

    private final UserRepository userRepository;

    private final ApplicationPermissionRepository applicationPermissionRepository;

    @Autowired
    public ApplicationController(UserRepository userRepository, ApplicationPermissionRepository applicationPermissionRepository){
        this.userRepository = userRepository;
        this.applicationPermissionRepository = applicationPermissionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplicationsForUserCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        Company userCompany = user.getCompany();

        List<Application> applications = applicationPermissionRepository.findByCompany(userCompany).stream()
                .map(ApplicationPermission::getApplication)
                .collect(Collectors.toList());

        return ResponseEntity.ok(applications);
    }
}
