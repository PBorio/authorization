package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.exceptions.CompanyNotFoundException;
import com.borio.authorization.domain.exceptions.CustomerNotFoundException;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.CustomerRepository;
import com.borio.authorization.repositories.ProposalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProposalServiceImpl implements ProposalService {

    private CompanyRepository companyRepository;
    private CustomerRepository customerRepository;
    private ProposalRepository proposalRepository;

    @Autowired
    public ProposalServiceImpl(CompanyRepository companyRepository,
                               CustomerRepository customerRepository,
                               ProposalRepository proposalRepository) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.proposalRepository = proposalRepository;
    }

    @Override
    public Proposal save(ProposalForm proposalForm) {

        try {
            Company company = findCompany(proposalForm);

            Customer customer;
            if (proposalForm.getCustomerId() != null && proposalForm.getCustomerId() != 0) {
                customer = findCustomer(proposalForm);
            } else {
                customer = saveCustomer(proposalForm, company);
            }

            Proposal proposal = new Proposal();
            proposal.setDescription(proposalForm.getDescription());
            proposal.setObservation(proposalForm.getObservation());
            proposal.setCompany(company);
            proposal.setCustomer(customer);

            return proposalRepository.save(proposal);
        } catch (CompanyNotFoundException | CustomerNotFoundException e) {
            log.error(e.getMessage(),e);
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new GeneralAuthorizationException(e.getMessage(),e);
        }
    }

    private Company findCompany(ProposalForm proposalForm) {
        Optional<Company> oCompany = companyRepository.findById(proposalForm.getCompanyId());

        if (oCompany.isEmpty()) {
            throw new CompanyNotFoundException("Company not found");
        }
        Company company = oCompany.get();
        return company;
    }

    private Customer saveCustomer(ProposalForm proposalForm, Company company) {
        Customer customer;
        if (proposalForm.getCustomerName() == null || proposalForm.getCustomerName().trim() == "") {
            throw new CustomerNotFoundException("Customer Not Informed");
        }

        customer = new Customer();
        customer.setName(proposalForm.getCustomerName());
        customer.setCompany(company);
        customer = customerRepository.save(customer);
        return customer;
    }

    private Customer findCustomer(ProposalForm proposalForm) {
        Customer customer;
        Optional<Customer> oCustomer = customerRepository.findById(proposalForm.getCustomerId());
        if (oCustomer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found");
        }
        customer = oCustomer.get();
        return customer;
    }
}
