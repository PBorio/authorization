package com.borio.authorization.controllers;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.controllers.forms.ProposalServiceItemForm;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.ProposalServiceItem;
import com.borio.authorization.helpers.ProposalStubHelper;
import com.borio.authorization.services.ProposalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProposalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProposalService proposalService;

    @Test
    public void shouldCreateANewProposal() throws Exception {

        Proposal proposal = ProposalStubHelper.proposalStub();
        ProposalForm proposalForm = new ProposalForm(null,
                "A Description",
                "A Observation",
                100.0,
                1L,
                "A Customer Name",
                1L);

        when(proposalService.save(proposalForm)).thenReturn(proposal);

        this.mockMvc.perform(post("/proposals")
                .content(new ObjectMapper().writeValueAsString(proposalForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void whenProposalDescriptionIsNotInformedShouldReturnError() throws Exception {

        ProposalForm proposalForm = new ProposalForm(null,
                null, //null description
                "A Observation",
                100.0,
                1L,
                "A Customer Name",
                1L);

        this.mockMvc.perform(post("/proposals")
                .content(new ObjectMapper().writeValueAsString(proposalForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCompanyIsNotInformedShouldReturnError() throws Exception {

        ProposalForm proposalForm = new ProposalForm(null,
                "Description",
                "A Observation",
                100.0,
                1L,
                "A Customer Name",
                null); //null company id

        this.mockMvc.perform(post("/proposals")
                .content(new ObjectMapper().writeValueAsString(proposalForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldAddAProposalServiceToAProposal() throws Exception {

        ProposalServiceItemForm proposalServiceItemForm = new ProposalServiceItemForm(null,
                "Description",
                "A Observation",
                100.0,
                1L);

        Proposal proposal = ProposalStubHelper.proposalStub();
        ProposalServiceItem psi = new ProposalServiceItem();
        psi.setId(101L);
        psi.setProposal(proposal);
        proposal.getItems().add(psi);
        psi.setDescription(proposalServiceItemForm.getDescription());
        psi.setObservation(proposalServiceItemForm.getObservation());
        psi.setValue(proposalServiceItemForm.getValue());

        when(proposalService.saveServiceItem(proposalServiceItemForm)).thenReturn(psi);
        this.mockMvc.perform(post("/proposals/1/service")
                .content(new ObjectMapper().writeValueAsString(proposalServiceItemForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void whenDescriptionIsNotInformedShouldReturnError() throws Exception {

        ProposalServiceItemForm proposalServiceItemForm = new ProposalServiceItemForm(null,
                null, //null description
                "A Observation",
                100.0,
                1L);

        this.mockMvc.perform(post("/proposals/1/service/1")
                .content(new ObjectMapper().writeValueAsString(proposalServiceItemForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldDeleteAServiceItem() throws Exception {

        Long proposalId = 1L;
        Long serviceId = 1L;

        this.mockMvc.perform(delete("/proposals/"+proposalId+"/service/"+serviceId))
                .andExpect(status().isOk());

        verify(proposalService, times(1)).deleteServiceItem(proposalId,serviceId);
    }

    @Test
    public void shouldReturnAProposalReport() throws Exception {

        Long proposalId = 1L;
        Long serviceId = 1L;

        this.mockMvc.perform(get("/proposals/"+proposalId+"/report/"))
                .andExpect(status().isOk());

        verify(proposalService, times(1)).generateReport(proposalId);
    }



}
