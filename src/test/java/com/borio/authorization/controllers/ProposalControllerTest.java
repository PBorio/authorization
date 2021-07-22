package com.borio.authorization.controllers;

import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.Proposal;
import com.borio.authorization.domain.User;
import com.borio.authorization.helpers.ProposalStubHelper;
import com.borio.authorization.services.ProposalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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

        this.mockMvc.perform(post("/proposal")
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

        this.mockMvc.perform(post("/proposal")
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

        this.mockMvc.perform(post("/proposal")
                .content(new ObjectMapper().writeValueAsString(proposalForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }



}
