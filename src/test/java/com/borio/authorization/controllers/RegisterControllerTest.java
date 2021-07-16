package com.borio.authorization.controllers;

import com.borio.authorization.domain.User;
import com.borio.authorization.domain.events.OnRegistrationCompleteEvent;
import com.borio.authorization.domain.exceptions.EmailAlreadyRegisteredException;
import com.borio.authorization.domain.exceptions.TokenExpiredException;
import com.borio.authorization.domain.exceptions.TokenNotFoundException;
import com.borio.authorization.services.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegisterService registerService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

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
    public void registrationShouldRegisterAnOnRegistrationCompleteEvent() throws Exception {

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

        verify(applicationEventPublisher,times(1)).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    public void shouldReturnAnErrorWhenEmailIsNotInformed() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(registerService.save(any(User.class))).thenReturn(user);

        this.mockMvc.perform(post("/register")
                .content("{ \"email\": \"\", \"password\": \"12345\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void shouldReturnAnErrorWhenEmailIsNotValid() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(registerService.save(any(User.class))).thenReturn(user);

        this.mockMvc.perform(post("/register")
                .content("{ \"email\": \"abcde\", \"password\": \"12345\" }")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

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

    }

    @Test
    public void shouldValidateTheTokenThatComesInTheRequest() throws Exception {

        User user = new User();
        user.setId(1L);

        String aValidToken = "ABCDEFG123456";

        when(registerService.validateToken(aValidToken)).thenReturn(user);

        this.mockMvc.perform(get("/register/complete?token="+aValidToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(registerService, times(1)).validateToken(aValidToken);
    }

    @Test
    public void shouldReturnErrorWhenTokenIsNotFound() throws Exception {

        String aInvalidToken = "XXXXXXXX----------";

        doThrow(TokenNotFoundException.class).
        when(registerService).validateToken(aInvalidToken);

        this.mockMvc.perform(get("/register/complete?token="+aInvalidToken))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void shouldReturnErrorWhenTokenIsExpired() throws Exception {

        String anExpiredToken = "XXXXXXXX----------";

        doThrow(TokenExpiredException.class).
                when(registerService).validateToken(anExpiredToken);

        this.mockMvc.perform(get("/register/complete?token="+anExpiredToken))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    /** Spring does now allow to Mock ApplicationEventPublisher as
     * a normal interface. Without this workaround it will inject
     * the true implementation instead of the mocked one
     */
    @TestConfiguration
    static class MockitoPublisherConfiguration {

        @Bean
        @Primary
        ApplicationEventPublisher publisher() {
            return mock(ApplicationEventPublisher.class);
        }
    }
}
