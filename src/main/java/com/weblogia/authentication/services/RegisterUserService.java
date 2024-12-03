package com.weblogia.authentication.services;

import com.weblogia.authentication.controller.records.AuthRequestDTO;
import com.weblogia.authentication.controller.records.RegisterUserAdminAndCompanyDTO;
import com.weblogia.authentication.controller.records.RegisterUserDto;
import com.weblogia.authentication.exceptions.CompanyNotInformedException;
import com.weblogia.authentication.model.Company;
import com.weblogia.authentication.model.User;
import com.weblogia.authentication.model.UserRole;
import com.weblogia.authentication.repositories.CompanyRepository;
import com.weblogia.authentication.repositories.UserRepository;
import com.weblogia.authentication.repositories.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterUserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                               CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUserAdmin(RegisterUserAdminAndCompanyDTO registerUserAdminAndCompanyDTO) {

        Company company = companyRepository.findByName(registerUserAdminAndCompanyDTO.companyName());
        if (company == null) {
            company = new Company();
            company.setName(registerUserAdminAndCompanyDTO.companyName());
            companyRepository.save(company);
        }

        User user = createUser(registerUserAdminAndCompanyDTO.username(), registerUserAdminAndCompanyDTO.password(), company, "ADMIN");

        registerUser(user);
    }

    private User createUser(String username, String password, Company company, String role) {
        UserRole userRole = userRoleRepository.findByName(role);

        User user = User.createUser(username, passwordEncoder.encode(password), company, userRole);

        return user;
    }


    public void registerUserSysAdmin(AuthRequestDTO registerRequestDTO) {
        UserRole userRole = userRoleRepository.findByName("SYS_ADMIN");

        User user =
                User.createSysAdminUser(registerRequestDTO.username(),
                                        passwordEncoder.encode(registerRequestDTO.password()),
                                        userRole);

        registerUser(user);
    }

    public void registerBasicUser(RegisterUserDto registerUserDto){
        Optional<Company> oCompany = companyRepository.findById(registerUserDto.companyId());
        if (oCompany.isEmpty()) {
            throw new CompanyNotInformedException("The informed Company was not informed");
        }

        User user = createUser(registerUserDto.username(), registerUserDto.password(), oCompany.get(), "USER");
        registerUser(user);
    }


    private void registerUser(User user) {
        userRepository.save(user);
    }


}
