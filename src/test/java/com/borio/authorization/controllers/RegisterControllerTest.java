package com.borio.authorization.controllers;

import com.borio.authorization.domain.User;
import com.borio.authorization.domain.exceptions.EmailAlreadyRegisteredException;
import com.borio.authorization.services.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;

    @Test
    public void shouldRegisterANewUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(registerService.save(any(User.class))).thenReturn(user);

        this.mockMvc.perform(post("/register")
                             .content("{ \"email\": \"test@test.com\", \"password\": \"12345\" }")
                             .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    public void registrationShouldSendAnEmail() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(registerService.save(any(User.class))).thenReturn(user);

        this.mockMvc.perform(post("/register")
                .content("{ \"email\": \"test@test.com\", \"password\": \"12345\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(registerService,times(1)).sendMail(user);
    }

    @Test
    public void shouldReturnErrorWhenAnEmailIsAlreadyRegistered() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(registerService.save(any(User.class))).thenThrow(EmailAlreadyRegisteredException.class);

        this.mockMvc.perform(post("/register")
                .content("{ \"email\": \"test@test.com\", \"password\": \"12345\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        verify(registerService,times(0)).sendMail(user);

    }

}
