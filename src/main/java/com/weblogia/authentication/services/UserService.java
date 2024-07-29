package com.weblogia.authentication.services;

import com.weblogia.authentication.controller.records.AuthRequestDTO;
import com.weblogia.authentication.controller.records.RegisterUserAdminAndCompanyDTO;
import com.weblogia.authentication.model.Company;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.model.UserRole;
import com.weblogia.authentication.repositories.CompanyRepository;
import com.weblogia.authentication.repositories.UserRepository;
import com.weblogia.authentication.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUserAdmin(RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO) {
        Company company = companyRepository.findByName(registerUserAdminAndCompanyDTO.companyName());
        if (company == null) {
            company = new Company();
            company.setName(registerUserAdminAndCompanyDTO.companyName());
            companyRepository.save(company);
        }

        // Criar novo usuário
        User user = new User();
        user.setUsername(registerUserAdminAndCompanyDTO.username());
        user.setPassword(passwordEncoder.encode(registerUserAdminAndCompanyDTO.password()));
        user.setCompany(company);

        // Definir role padrão (por exemplo, USER)
        UserRole userRole = userRoleRepository.findByName("ADMIN");
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    public void registerUserSysAdmin(AuthRequestDTO registerRequestDTO) {
        registerUser(registerRequestDTO, "SYS_ADMIN");
    }

    private void registerUser(AuthRequestDTO registerRequestDTO, String role) {
        User user = new User();
        user.setUsername(registerRequestDTO.username());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));

        UserRole userRole = userRoleRepository.findByName(role);
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
    }


}
