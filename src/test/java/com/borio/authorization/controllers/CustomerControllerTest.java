package com.borio.authorization.controllers;

import com.borio.authorization.controllers.forms.CustomerForm;
import com.borio.authorization.controllers.forms.ProposalForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.Customer;
import com.borio.authorization.domain.User;
import com.borio.authorization.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Test
    public void shouldCreateANewCustomer() throws Exception {

        CustomerForm customerForm = new CustomerForm();
        customerForm.setName("Customer Test");
        customerForm.setCompanyId(1L);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName(customerForm.getName());

        when(customerService.save(customerForm)).thenReturn(customer);

        this.mockMvc.perform(post("/customers")
                .content(new ObjectMapper().writeValueAsString(customerForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    public void whenCustomerNameIsNotInformedShouldReturnError() throws Exception {

        CustomerForm customerForm = new CustomerForm();
        customerForm.setCompanyId(1L);

        this.mockMvc.perform(post("/customers")
                .content(new ObjectMapper().writeValueAsString(customerForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldFindACustomerWhenItsIdIsGiven() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer Test");


        when(customerService.findById(customer.getId())).thenReturn(customer);

        this.mockMvc.perform(get("/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

    }

}
