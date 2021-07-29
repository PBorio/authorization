package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.CustomerForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.exceptions.CompanyNotFoundException;
import com.borio.authorization.domain.exceptions.CustomerNotFoundException;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.ResourceNotFoundException;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.CustomerRepository;
import com.borio.authorization.repositories.ProposalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{

    private CompanyService companyService;
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CompanyService companyService,
                               CustomerRepository customerRepository) {
        this.companyService = companyService;
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(CustomerForm customerForm) {

        try {
            Company company = companyService.findById(customerForm.getCompanyId());

            Customer customer = new Customer();
            customer.setName(customerForm.getName());
            customer.setAddress(customerForm.getAddress());
            customer.setEmail(customerForm.getEmail());
            customer.setPhoneNumber(customerForm.getPhoneNumber());
            customer.setCompany(company);

            return customerRepository.save(customer);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(),e);
            throw new CompanyNotFoundException(e.getMessage(),e);
        } catch (RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new GeneralAuthorizationException(e.getMessage(),e);
        }
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> oCustomer = this.customerRepository.findById(id);

        if (oCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found");
        }

        return oCustomer.get();
    }
}
