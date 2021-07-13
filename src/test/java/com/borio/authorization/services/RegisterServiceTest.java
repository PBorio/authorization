package com.borio.authorization.services;

import com.borio.authorization.domain.User;
import com.borio.authorization.domain.VerificationToken;
import com.borio.authorization.domain.exceptions.EmailAlreadyRegisteredException;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.TokenNotFoundException;
import com.borio.authorization.repositories.TokenRepository;
import com.borio.authorization.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegisterServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    RegisterServiceImpl registerService;

    @Test
    public void shouldSaveANewUserAndGiveItBackToTheClient(){
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setEmail("test@test.com");
        createdUser.setPassword("12345");

        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        User result = registerService.save(createdUser);

        assertNotNull(result);
    }

    @Test
    public void givenAnEmailIsAlreadyRegisteredShouldThrowEmailAlreadyRegisteredException(){

        User alreadyRegisteredUser = new User();
        alreadyRegisteredUser.setEmail("test@test.com");
        List<User> foundedUsers = new ArrayList<>();
        foundedUsers.add(alreadyRegisteredUser);

        when(userRepository.findByEmail("test@test.com")).thenReturn(foundedUsers);

        assertThrows(EmailAlreadyRegisteredException.class, () ->
                registerService.save(alreadyRegisteredUser));

    }

    @Test
    public void forAnyExceptionInUserRepositorySaveMethodWillThrowAGeneralAuthorizationException(){

        User user = new User();

        when(userRepository.save(user)).thenThrow(RuntimeException.class);

        assertThrows(GeneralAuthorizationException.class, () ->
                registerService.save(user));

    }

    @Test
    public void shouldCreateANewValidationToken(){
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("12345");

        String token = "ABCDEFG123456";

        registerService.createValidationToken(user,token, new Date());

        verify(tokenRepository, times(1)).save(any(VerificationToken.class));

    }

    @Test
    public void forAnyExceptionInTokenRepositorySaveMethodWillThrowAGeneralAuthorizationException(){

        User user = new User();
        String token = "ABDD1234";


        when(tokenRepository.save(any(VerificationToken.class))).thenThrow(RuntimeException.class);

        assertThrows(GeneralAuthorizationException.class, () ->
                registerService.createValidationToken(user,token, new Date()));

    }

    @Test
    public void shouldValidateTheTokenWhenTheExpireDateIsAfterNow() {

        User user = new User();
        user.setId(1L);

        VerificationToken verificationToken = new VerificationToken();
        String token = "ABDD1234";
        verificationToken.setId(1L);
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        List<VerificationToken> dbResult = new ArrayList<>();
        dbResult.add(verificationToken);


        when(tokenRepository.findByToken(eq(token), any(Date.class))).thenReturn(dbResult);

        registerService.validateToken(token);

        verify(tokenRepository, times(1)).findByToken(eq(token), any(Date.class));
        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void shouldThrowTokenNotFoundExceptionIfNoTokenIsFound() {

        User user = new User();
        user.setId(1L);

        VerificationToken verificationToken = new VerificationToken();
        String token = "ABDD1234";
        verificationToken.setId(1L);
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        List<VerificationToken> dbResult = new ArrayList<>();

        when(tokenRepository.findByToken(eq(token), any(Date.class))).thenReturn(dbResult);

        assertThrows(TokenNotFoundException.class, () ->
                registerService.validateToken(token));

    }



}
