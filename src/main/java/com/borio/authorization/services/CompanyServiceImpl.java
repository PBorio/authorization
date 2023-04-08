package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.controllers.forms.LogoForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.User;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.ResourceNotFoundException;
import com.borio.authorization.domain.exceptions.SystemAuthorizationException;
import com.borio.authorization.domain.exceptions.UserNotFoundException;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private UserRepository userRepository;

    private CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Company create(CompanyForm companyForm) {

        try {
            Company company = new Company();
            company.setName(companyForm.getName());
            company.setAlias(companyForm.getAlias());
            //company.setLogo(companyForm.getLogo().getBytes());

            Optional<User> oUser = userRepository.findById(companyForm.getUserId());

            if (oUser.isEmpty()) {
                throw new UserNotFoundException("No User could be found with ID "+companyForm.getUserId());
            }

            User user = oUser.get();

            company.setUser(user);

            return companyRepository.save(company);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(),e);
            throw e;
        } catch(RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new GeneralAuthorizationException(e.getMessage(),e);
        }
    }

    @Override
    public Company findById(Long id) {
        Optional<Company> oCompany = this.companyRepository.findById(id);

        if (oCompany.isEmpty()) {
            throw new ResourceNotFoundException("Company not found");
        }

        return oCompany.get();
    }

    @Override
    public Company addLogo(LogoForm logoForm) {
        return null;
    }
}
