package com.weblogia.authentication.services;

import com.weblogia.authentication.controller.records.AuthRequestDTO;
import com.weblogia.authentication.controller.records.RegisterUserAdminAndCompanyDTO;
import com.weblogia.authentication.model.Company;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.model.UserRole;
import com.weblogia.authentication.repositories.CompanyRepository;
import com.weblogia.authentication.repositories.UserRepository;
import com.weblogia.authentication.repositories.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUserAdmin(RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO) {
        registerUser(registerUserAdminAndCompanyDTO, "ADMIN");
    }



    public void registerUserSysAdmin(AuthRequestDTO registerRequestDTO) {
        User user = new User();
        user.setUsername(registerRequestDTO.username());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));

        UserRole userRole = userRoleRepository.findByName("SYS_ADMIN");
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    public void registerBasicUser(RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO){
        registerUser(registerUserAdminAndCompanyDTO, "USER");
    }


    private void registerUser(RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO, String role) {
        Company company = companyRepository.findByName(registerUserAdminAndCompanyDTO.companyName());
        if (company == null) {
            company = new Company();
            company.setName(registerUserAdminAndCompanyDTO.companyName());
            companyRepository.save(company);
        }

        User user = new User();
        user.setUsername(registerUserAdminAndCompanyDTO.username());
        user.setPassword(passwordEncoder.encode(registerUserAdminAndCompanyDTO.password()));
        user.setCompany(company);

        UserRole userRole = userRoleRepository.findByName(role);
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }


}
