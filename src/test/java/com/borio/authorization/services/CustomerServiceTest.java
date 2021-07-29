package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.CustomerForm;
import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.exceptions.CompanyNotFoundException;
import com.borio.authorization.domain.exceptions.ResourceNotFoundException;
import com.borio.authorization.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CompanyService companyService;

    @InjectMocks
    CustomerServiceImpl customerService;

    @Test
    public void shouldSaveANewCustomerAndGiveItBackToTheClient(){

        CustomerForm customerForm = new CustomerForm(null,
                "CUSTOMER NAME",
                null,
                null,
                null, null, 1L);

        Customer createdCustomer = new Customer();
        createdCustomer.setName(customerForm.getName());
        Company company = new Company();
        company.setId(1L);
        createdCustomer.setCompany(company);

        when(companyService.findById(customerForm.getCompanyId())).thenReturn(company);
        when(customerRepository.save(any(Customer.class))).thenReturn(createdCustomer);

        Customer result = customerService.save(customerForm);

        assertNotNull(result);
    }

    @Test
    public void whenCompanyIsNotFoundShouldThrowCompanyNotFoundException(){

        CustomerForm customerForm = new CustomerForm(null,
                "CUSTOMER NAME",
                null,
                null,
                null, null,
                9999L); // a no existent company Id

        when(companyService.findById(customerForm.getCompanyId())).thenThrow(ResourceNotFoundException.class);

        assertThrows(CompanyNotFoundException.class, () ->
                customerService.save(customerForm));
    }

    @Test
    public void findByIdShouldReturnTheCompanyForTheId(){

        Long customerId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test");
        Optional<Customer> optionalCustomer = Optional.of(customer);

        when(customerRepository.findById(customerId)).thenReturn(optionalCustomer);

        Customer result = customerService.findById(customerId);

        assertNotNull(result);
    }

    @Test
    public void whenNoCustomerIsFoundInFindByIdShouldThrowResourceNotFoundException(){

        Long wrongCustomerId = 0_000L;
        Optional<Customer> emptyResult = Optional.empty();

        when(customerRepository.findById(wrongCustomerId)).thenReturn(emptyResult);

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.findById(wrongCustomerId));
    }
}
