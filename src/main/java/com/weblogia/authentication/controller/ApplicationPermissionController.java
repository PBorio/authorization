package com.weblogia.authentication.controller;

import com.weblogia.authentication.controller.records.ChangePassDto;
import com.weblogia.authentication.model.Application;
import com.weblogia.authentication.model.ApplicationPermission;
import com.weblogia.authentication.model.Company;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.repositories.ApplicationPermissionRepository;
import com.weblogia.authentication.repositories.ApplicationRepository;
import com.weblogia.authentication.repositories.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/permissions")
public class ApplicationPermissionController {

    private final ApplicationPermissionRepository applicationPermissionRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;


    @Autowired
    private ApplicationPermissionController(ApplicationPermissionRepository applicationPermissionRepository,
                                            ApplicationRepository applicationRepository,
                                            CompanyRepository companyRepository) {
        this.applicationPermissionRepository = applicationPermissionRepository;
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
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

    @PutMapping("/sys-admin/add-permission/{applicationId}")
    public ResponseEntity<String> addPermission(@PathVariable Long applicationId, @AuthenticationPrincipal User loggedUser) {
        Optional<Application> oApplication = applicationRepository.findById(applicationId);
        Optional<Company> oCompany = companyRepository.findById(loggedUser.getCompany().getId());
        if (oApplication.isPresent() && oCompany.isPresent()) {
            Application app = oApplication.get();
            Company company = oCompany.get();
            ApplicationPermission applicationPermission = new ApplicationPermission(app, company);
            applicationPermissionRepository.save(applicationPermission);

            return ResponseEntity.ok(String.format("Permission for %s added", app.getName()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private ResponseEntity<List<ApplicationPermission>> notAuthorized() {
        return ResponseEntity.status(401).build();
    }
}
