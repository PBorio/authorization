package com.borio.authorization;

import com.borio.authorization.domain.User;
import com.borio.authorization.services.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
@RunWith(MockitoJUnitRunner.class)*/
public class UserRegisterStepDef {

    /*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterService registerService;

    ResultActions resultActions;


    private User user = new User();


    @When("user gives the email {string}")
    public void user_gives_the_email(String email) throws Throwable {
        user.setEmail(email);
    }

    @And("user gives the password {string}")
    public void user_gives_the_password(String password) throws Throwable {
        user.setPassword(password);
    }

    @And("there is no user registered with this email")
    public void there_is_no_user_for_the_email() throws Throwable {

        User createdUser = new User();
        createdUser.setId(1l);
        createdUser.setEmail(user.getEmail());

        when(registerService.save(any(User.class))).thenReturn(createdUser);
    }

    @And("user makes a POST to {string}")
    public void user_makes_Post_to(String context) throws Throwable{
        resultActions = this.mockMvc.perform(post(context)
                .content(asJsonString(this.user))
                .contentType(MediaType.APPLICATION_JSON));
    }


    @Then("the client receives status code {int}")
    public void the_client_receives_the_code(Integer code) throws Throwable {
        resultActions
              .andDo(print())
              .andExpect(status().isCreated());
    }

    @And("the application sends an email to the user email")
    public void the_application_sends_an_email_to_the_user() throws Throwable {
        verify(registerService, times(1)).sendMail(user);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

}
