package com.borio.authorization.services;


import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.User;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.ResourceNotFoundException;
import com.borio.authorization.domain.exceptions.UserNotFoundException;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.TokenRepository;
import com.borio.authorization.repositories.UserRepository;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CompanyServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyServiceImpl companyService;

    @Test
    public void shouldSaveANewCompanyAndGiveItBackToTheClient(){

        CompanyForm companyForm = new CompanyForm();
        companyForm.setUserId(1L);
        companyForm.setAlias("company_test");
        companyForm.setName("Test Company Inc.");

        User user = new User();
        user.setId(1L);

        Company createdCompany = new Company();
        createdCompany.setId(1L);
        createdCompany.setUser(user);
        createdCompany.setAlias("company_test");
        createdCompany.setName("Test Company Inc.");

        when(userRepository.findById(companyForm.getUserId())).thenReturn(Optional.of(user));
        when(companyRepository.save(any(Company.class))).thenReturn(createdCompany);

        Company result = companyService.create(companyForm);

        assertNotNull(result);
    }

    @Test
    public void forAnyExceptionInCompanyRepositorySaveMethodWillThrowAGeneralAuthorizationException(){

        CompanyForm companyForm = new CompanyForm();

        Optional<User> optional = Optional.of(new User());
        when(userRepository.findById(companyForm.getUserId())).thenReturn(optional);
        when(companyRepository.save(any(Company.class))).thenThrow(RuntimeException.class);

        assertThrows(GeneralAuthorizationException.class, () ->
                companyService.create(companyForm));
    }

    @Test
    public void whenNoUserIsFoundShouldThrowUserNotFoundException(){

        CompanyForm companyForm = new CompanyForm();
        companyForm.setUserId(1L);
        Optional<User> optional = Optional.empty();

        when(userRepository.findById(companyForm.getUserId())).thenReturn(optional);

        assertThrows(UserNotFoundException.class, () ->
                companyService.create(companyForm));
    }

    @Test
    public void findByIdShouldReturnTheCompanyForTheId(){

        Long companyId = 1L;

        Company company = new Company();
        company.setId(companyId);
        company.setName("Test");
        Optional<Company> optionalCompany = Optional.of(company);

        when(companyRepository.findById(companyId)).thenReturn(optionalCompany);

        Company result = companyService.findById(companyId);

        assertNotNull(result);
    }

    @Test
    public void whenNoCompanyIsFoundInFindByIdShouldThrowResourceNotFoundException(){

        Long wrongCompanyId = 0_000L;
        Optional<Company> emptyResult = Optional.empty();

        when(companyRepository.findById(wrongCompanyId)).thenReturn(emptyResult);

        assertThrows(ResourceNotFoundException.class, () ->
                companyService.findById(wrongCompanyId));
    }

}
