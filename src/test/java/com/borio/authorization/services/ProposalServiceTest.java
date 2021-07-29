package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.controllers.forms.ProposalServiceItemForm;
import com.borio.authorization.domain.*;
import com.borio.authorization.domain.exceptions.CompanyNotFoundException;
import com.borio.authorization.domain.exceptions.CustomerNotFoundException;
import com.borio.authorization.domain.exceptions.ProposalNotFoundException;
import com.borio.authorization.domain.exceptions.ResourceNotFoundException;
import com.borio.authorization.helpers.ProposalStubHelper;
import com.borio.authorization.repositories.CompanyRepository;
import com.borio.authorization.repositories.CustomerRepository;
import com.borio.authorization.repositories.ProposalRepository;
import com.borio.authorization.repositories.ProposalServiceItemRepository;
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
    CompanyService companyService;

    @Mock
    CustomerRepository customerRepository;


    @Mock
    ProposalServiceItemRepository proposalServiceItemRepository;

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

        when(companyService.findById(proposalForm.getCompanyId())).thenReturn(company);
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

        when(companyService.findById(proposalForm.getCompanyId())).thenThrow(ResourceNotFoundException.class);

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

        when(companyService.findById(proposalForm.getCompanyId())).thenReturn(company);
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

        when(companyService.findById(proposalForm.getCompanyId())).thenReturn(company);
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

        when(companyService.findById(proposalForm.getCompanyId())).thenReturn(company);
        when(customerRepository.findById(proposalForm.getCustomerId())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                proposalService.save(proposalForm));
    }

    @Test
    public void shouldSaveProposalServiceItem(){

        ProposalServiceItemForm proposalServiceItemForm = new ProposalServiceItemForm(null,
                null, //null description
                "A Observation",
                100.0,
                1L);

        Proposal proposal = ProposalStubHelper.proposalStub();
        proposalServiceItemForm.setProposalId(proposal.getId());

        ProposalServiceItem createdServiceItem = new ProposalServiceItem();

        when(proposalRepository.findById(proposalServiceItemForm.getProposalId())).thenReturn(Optional.of(proposal));
        when(proposalServiceItemRepository.save(any(ProposalServiceItem.class))).thenReturn(createdServiceItem);

        ProposalServiceItem result = proposalService.saveServiceItem(proposalServiceItemForm);

        assertNotNull(result);
        verify(proposalServiceItemRepository, times(1)).save(any(ProposalServiceItem.class));
    }

    @Test
    public void shouldDeleteAServiceItem(){

        Long proposalId = 1L;
        Long serviceId = 1L;
        ProposalServiceItem proposalServiceItem = new ProposalServiceItem();
        proposalServiceItem.setId(serviceId);

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(new Proposal()));
        when(proposalServiceItemRepository.findById(serviceId)).thenReturn(Optional.of(proposalServiceItem));

        proposalService.deleteServiceItem(proposalId,serviceId);

        verify(proposalServiceItemRepository, times(1)).delete(proposalServiceItem);
    }

    @Test
    public void whenProposalIsNotFoundShouldThrowResourceNotFoundException(){

        Long proposalId = 1L;
        Long serviceId = 1L;

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                proposalService.deleteServiceItem(proposalId,serviceId));

    }

    @Test
    public void whenServiceIsNotFoundShouldThrowResourceNotFoundException(){

        Long proposalId = 1L;
        Long serviceId = 1L;

        when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(new Proposal()));
        when(proposalServiceItemRepository.findById(serviceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                proposalService.deleteServiceItem(proposalId,serviceId));

    }

}
