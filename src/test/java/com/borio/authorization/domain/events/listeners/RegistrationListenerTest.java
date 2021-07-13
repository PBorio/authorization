package com.borio.authorization.domain.events.listeners;


import com.borio.authorization.domain.User;
import com.borio.authorization.domain.events.OnRegistrationCompleteEvent;
import com.borio.authorization.services.RegisterService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RegistrationListenerTest {


    @Mock
    RegisterService registerService;

    @Mock
    MessageSource messageSource;

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    RegistrationListener registrationListener = new RegistrationListener();

    @Test
    public void onApplicationEventShouldCreateANewValidationToken() {
        registrationListener.onApplicationEvent(new OnRegistrationCompleteEvent(new User(), Locale.GERMANY, "", new Date()));
        verify(registerService,times(1)).createValidationToken(any(User.class), anyString(), any(Date.class));
    }

    @Test
    public void onApplicationEventShouldCreateAnEmailMessage() {
        registrationListener.onApplicationEvent(new OnRegistrationCompleteEvent(new User(), Locale.GERMANY, "", new Date()));
        verify(messageSource,times(1)).getMessage(anyString(), any(), any(Locale.class));
    }

    @Test
    public void onApplicationEventShouldSendAnEmailToTheUser() {
        User user = new User("test@test.com", "12345", false);
        registrationListener.onApplicationEvent(new OnRegistrationCompleteEvent(user, Locale.GERMANY, "", new Date()));
        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }

}
