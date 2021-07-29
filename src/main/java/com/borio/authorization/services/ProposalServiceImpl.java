package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.controllers.forms.ProposalServiceItemForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.ProposalServiceItem;
import com.borio.authorization.domain.exceptions.*;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.CustomerRepository;
import com.borio.authorization.repositories.ProposalRepository;
import com.borio.authorization.repositories.ProposalServiceItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Service
@Slf4j
public class ProposalServiceImpl implements ProposalService {

    private CompanyService companyService;
    private CustomerRepository customerRepository;
    private ProposalRepository proposalRepository;
    private ProposalServiceItemRepository proposalServiceItemRepository;

    @Autowired
    public ProposalServiceImpl(CompanyService companyService,
                               CustomerRepository customerRepository,
                               ProposalRepository proposalRepository,
                               ProposalServiceItemRepository proposalServiceItemRepository) {
        this.companyService = companyService;
        this.customerRepository = customerRepository;
        this.proposalRepository = proposalRepository;
        this.proposalServiceItemRepository = proposalServiceItemRepository;
    }

    @Override
    public Proposal save(ProposalForm proposalForm) {

        try {
            Company company = companyService.findById(proposalForm.getCompanyId());

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
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(),e);
            throw new CompanyNotFoundException(e.getMessage(),e);
        }
        catch (CompanyNotFoundException | CustomerNotFoundException e) {
            log.error(e.getMessage(),e);
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new GeneralAuthorizationException(e.getMessage(),e);
        }
    }

    @Override
    public ProposalServiceItem saveServiceItem(ProposalServiceItemForm proposalServiceItemForm) {
        ProposalServiceItem proposalServiceItem = new ProposalServiceItem();
        proposalServiceItem.setDescription(proposalServiceItemForm.getDescription());
        proposalServiceItem.setObservation(proposalServiceItemForm.getObservation());
        proposalServiceItem.setValue(proposalServiceItemForm.getValue());

        Optional<Proposal> oProposal = this.proposalRepository.findById(proposalServiceItemForm.getProposalId());

        if (oProposal.isEmpty()) {
            throw new ProposalNotFoundException("Proposal Not Found");
        }

        proposalServiceItem.setProposal(oProposal.get());

        return proposalServiceItemRepository.save(proposalServiceItem);
    }

    @Override
    public void deleteServiceItem(Long proposalId, Long serviceId) {

        Optional<Proposal> optionalProposal = proposalRepository.findById(proposalId);
        if (optionalProposal.isEmpty()) {
            throw new ResourceNotFoundException("Proposal not found");
        }

        Optional<ProposalServiceItem> optionalProposalServiceItem = proposalServiceItemRepository.findById(proposalId);
        if (optionalProposalServiceItem.isEmpty()) {
            throw new ResourceNotFoundException("Service Item not found");
        }

        proposalServiceItemRepository.delete(optionalProposalServiceItem.get());
    }

    @Override
    public Proposal findById(Long id) {
        Optional<Proposal> oProposal = this.proposalRepository.findById(id);

        if (oProposal.isEmpty()) {
            throw new ResourceNotFoundException("Proposal not found");
        }

        return oProposal.get();
    }


    private Customer saveCustomer(ProposalForm proposalForm, Company company) {
        Customer customer;
        if (proposalForm.getCustomerName() == null || "".equals(proposalForm.getCustomerName().trim()) ) {
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
