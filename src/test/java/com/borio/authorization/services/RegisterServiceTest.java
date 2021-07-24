package com.borio.authorization.services;

import com.borio.authorization.config.security.SecurityConstants;
import com.borio.authorization.domain.Role;
import com.borio.authorization.domain.User;
import com.borio.authorization.domain.VerificationToken;
import com.borio.authorization.domain.exceptions.AdminRoleNotFoundException;
import com.borio.authorization.domain.exceptions.EmailAlreadyRegisteredException;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.TokenNotFoundException;
import com.borio.authorization.repositories.RoleRepository;
import com.borio.authorization.repositories.TokenRepository;
import com.borio.authorization.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenRepository tokenRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RegisterServiceImpl registerService;

    @Test
    public void shouldSaveANewUserAndGiveItBackToTheClient(){
        Role role = new Role();
        Optional<Role> optionalRole = Optional.of(role);

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setEmail("test@test.com");
        createdUser.setPassword("12345");

        when(roleRepository.findByName(SecurityConstants.COMPANY_ADMIN_ROLE)).thenReturn(optionalRole);
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        User result = registerService.save(createdUser);

        assertNotNull(result);
        assertFalse(result.getRoles().isEmpty());
    }

    @Test
    public void givenAnEmailIsAlreadyRegisteredShouldThrowEmailAlreadyRegisteredException(){

        Role role = new Role();
        Optional<Role> optionalRole = Optional.of(role);

        User alreadyRegisteredUser = new User();
        alreadyRegisteredUser.setEmail("test@test.com");
        Optional<User> oUser = Optional.of(alreadyRegisteredUser);

        when(roleRepository.findByName(SecurityConstants.COMPANY_ADMIN_ROLE)).thenReturn(optionalRole);
        when(userRepository.findByEmail("test@test.com")).thenReturn(oUser);

        assertThrows(EmailAlreadyRegisteredException.class, () ->
                registerService.save(alreadyRegisteredUser));

    }

    @Test
    public void givenNoCompanyAdminRoleExistsShouldThrowAdminRoleNotFoundException(){

        Optional<Role> optionalRole = Optional.empty();

        User alreadyRegisteredUser = new User();
        alreadyRegisteredUser.setEmail("test@test.com");
        Optional<User> oUser = Optional.of(alreadyRegisteredUser);

        when(roleRepository.findByName(SecurityConstants.COMPANY_ADMIN_ROLE)).thenReturn(optionalRole);
        when(userRepository.findByEmail("test@test.com")).thenReturn(oUser);

        assertThrows(AdminRoleNotFoundException.class, () ->
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
