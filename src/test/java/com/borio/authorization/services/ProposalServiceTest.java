package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.*;
import com.borio.authorization.domain.exceptions.CompanyNotFoundException;
import com.borio.authorization.domain.exceptions.CustomerNotFoundException;
import com.borio.authorization.helpers.ProposalStubHelper;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.CustomerRepository;
import com.borio.authorization.repositories.ProposalRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProposalServiceTest {

    @Mock
    ProposalRepository proposalRepository;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    ProposalServiceImpl proposalService;


    @Test
    public void shouldSaveANewProposalAndGiveItBackToTheClient(){

        ProposalForm proposalForm = new ProposalForm(null,
                "A Description",
                "A Observation",
                100.0,
                1L,
                "A Customer Name",
                1L);

        Proposal createdProposal = ProposalStubHelper.proposalStub();
        Company company = createdProposal.getCompany();
        Customer customer = createdProposal.getCustomer();

        when(companyRepository.findById(proposalForm.getCompanyId())).thenReturn(Optional.of(company));
        when(customerRepository.findById(proposalForm.getCustomerId())).thenReturn(Optional.of(customer));
        when(proposalRepository.save(any(Proposal.class))).thenReturn(createdProposal);

        Proposal result = proposalService.save(proposalForm);

        assertNotNull(result);
    }

    @Test
    public void whenCompanyIsNotFoundShouldThrowCompanyNotFoundException(){

        ProposalForm proposalForm = new ProposalForm(null,
                "A Description",
                "A Observation",
                100.0,
                1L,
                "A Customer Name",
                9999L); // a no existent company Id

        when(companyRepository.findById(proposalForm.getCompanyId())).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () ->
                proposalService.save(proposalForm));
    }

    @Test
    public void whenCustomerIdIsNotInformedThenANewCustomerShouldBeCreated(){

        ProposalForm proposalForm = new ProposalForm(null,
                "A Description",
                "A Observation",
                100.0,
                null, // no customer Id
                "A Customer Name",
                9999L);

        Proposal createdProposal = ProposalStubHelper.proposalStub();
        Company company = createdProposal.getCompany();
        Customer customer = createdProposal.getCustomer();

        when(companyRepository.findById(proposalForm.getCompanyId())).thenReturn(Optional.of(company));
        when(proposalRepository.save(any(Proposal.class))).thenReturn(createdProposal);

        Proposal result = proposalService.save(proposalForm);

        assertNotNull(result.getCustomer());
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    public void whenACustomerIdIsGivenButNotFoundShouldThrowCustomerNotFoundException(){

        ProposalForm proposalForm = new ProposalForm(null,
                "A Description",
                "A Observation",
                100.0,
                9999L, // a no existent customer Id
                "A Customer Name",
                1L); // a no existent company Id

        Proposal createdProposal = ProposalStubHelper.proposalStub();
        Company company = createdProposal.getCompany();
        Customer customer = createdProposal.getCustomer();

        when(companyRepository.findById(proposalForm.getCompanyId())).thenReturn(Optional.of(company));
        when(customerRepository.findById(proposalForm.getCustomerId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                proposalService.save(proposalForm));
    }

    @Test
    public void whenNeitherCustomerIdNorCustomerNameIsInformedShouldThrowCustomerNotFoundException(){

        ProposalForm proposalForm = new ProposalForm(null,
                "A Description",
                "A Observation",
                100.0,
                null, // a no existent customer Id
                null,
                1L); // a no existent company Id

        Proposal createdProposal = ProposalStubHelper.proposalStub();
        Company company = createdProposal.getCompany();
        Customer customer = createdProposal.getCustomer();

        when(companyRepository.findById(proposalForm.getCompanyId())).thenReturn(Optional.of(company));
        when(customerRepository.findById(proposalForm.getCustomerId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                proposalService.save(proposalForm));
    }

}
