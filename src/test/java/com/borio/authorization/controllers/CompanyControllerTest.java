package com.borio.authorization.controllers;

import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.User;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.ResourceNotFoundException;
import com.borio.authorization.services.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CompanyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CompanyService companyService;

    @Test
    public void shouldCreateANewCompany() throws Exception {

        User user = new User();
        user.setId(1L);
        Company company = new Company();
        company.setId(1L);
        company.setUser(user);
        company.setAlias("empresa_teste");
        company.setName("Empresa Teste LTDA");


        when(companyService.create(any(CompanyForm.class))).thenReturn(company);

        this.mockMvc.perform(post("/companies")
                .content("{ \"userId\": \"1\", " +
                        "\"name\": \"EMPRESA TESTE LTDA\", " +
                        "\"alias\": \"empresa_teste\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }


    @Test
    public void shouldReturnAnErrorWhenNameIsNotInformed() throws Exception {

        User user = new User();
        user.setId(1L);
        Company company = new Company();
        company.setId(1L);
        company.setUser(user);
        company.setAlias("empresa_teste");
        company.setName("Empresa Teste LTDA");

        when(companyService.create(any(CompanyForm.class))).thenReturn(company);

        this.mockMvc.perform(post("/companies")
                .content("{ \"userId\": \"1\", " +
                        "\"alias\": \"empresa_teste\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void shouldReturnAnErrorWhenAliasIsNotInformed() throws Exception {

        User user = new User();
        user.setId(1L);
        Company company = new Company();
        company.setId(1L);
        company.setUser(user);
        company.setAlias("empresa_teste");
        company.setName("Empresa Teste LTDA");

        when(companyService.create(any(CompanyForm.class))).thenReturn(company);

        this.mockMvc.perform(post("/companies")
                .content("{ \"userId\": \"1\", " +
                        "\"name\": \"EMPRESA TESTE LTDA\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void shouldReturnAnErrorWhenAliasHasWrongCharacters() throws Exception {

        User user = new User();
        user.setId(1L);
        Company company = new Company();
        company.setId(1L);
        company.setUser(user);
        company.setAlias("empresateste");
        company.setName("Empresa Teste LTDA");

        when(companyService.create(any(CompanyForm.class))).thenReturn(company);

        this.mockMvc.perform(post("/companies")
                .content("{ \"userId\": \"1\", " +
                        "\"name\": \"EMPRESA TESTE LTDA\", " +
                        "\"alias\": \"empresa-teste\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void shouldReturnAnErrorWhenUserIsNotInformed() throws Exception {

        User user = new User();
        user.setId(1L);
        Company company = new Company();
        company.setId(1L);
        company.setUser(user);
        company.setAlias("empresateste");
        company.setName("Empresa Teste LTDA");

        when(companyService.create(any(CompanyForm.class))).thenReturn(company);

        this.mockMvc.perform(post("/companies")
                .content("{ " +
                        "\"name\": \"EMPRESA TESTE LTDA\", " +
                        "\"alias\": \"empresa-teste\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void shouldFindACompanyWhenItsIdIsGiven() throws Exception {

        User user = new User();
        user.setId(1L);
        Company company = new Company();
        company.setId(1L);
        company.setUser(user);
        company.setAlias("empresa_teste");
        company.setName("Empresa Teste LTDA");


        when(companyService.findById(company.getId())).thenReturn(company);

        this.mockMvc.perform(get("/companies/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    public void shouldReturn404WhenCompanyNotFound() throws Exception {

        when(companyService.findById(000_000L)).thenThrow(ResourceNotFoundException.class);

        this.mockMvc.perform(get("/companies/000000"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


}
