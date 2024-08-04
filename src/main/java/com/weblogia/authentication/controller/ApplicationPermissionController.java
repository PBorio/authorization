package com.weblogia.authentication.controller;

import com.weblogia.authentication.model.ApplicationPermission;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.repositories.ApplicationPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class ApplicationPermissionController {

    private final ApplicationPermissionRepository applicationPermissionRepository;



    @Autowired
    private ApplicationPermissionController(ApplicationPermissionRepository applicationPermissionRepository) {
        this.applicationPermissionRepository = applicationPermissionRepository;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationPermission>> getPermissions(@AuthenticationPrincipal User loggedUser) {
        if (loggedUser == null) {
            return notAuthorized();
        }

        List<ApplicationPermission> applicationPermissions = applicationPermissionRepository.findByCompany(loggedUser.getCompany());

        if (applicationPermissions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(applicationPermissions);
    }

    private ResponseEntity<List<ApplicationPermission>> notAuthorized() {
        return ResponseEntity.status(401).build();
    }
}
