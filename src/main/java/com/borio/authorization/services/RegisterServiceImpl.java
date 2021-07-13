package com.borio.authorization.services;

import com.borio.authorization.domain.User;
import com.borio.authorization.domain.VerificationToken;
import com.borio.authorization.domain.exceptions.EmailAlreadyRegisteredException;
import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import com.borio.authorization.domain.exceptions.TokenNotFoundException;
import com.borio.authorization.repositories.TokenRepository;
import com.borio.authorization.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {


    private UserRepository userRepository;


    private TokenRepository tokenRepository;

    @Autowired
    public RegisterServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User save(User user) {

        List<User> users = userRepository.findByEmail(user.getEmail());

        if (!users.isEmpty()) {
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }

        try {
            return userRepository.save(user);
        } catch (RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new GeneralAuthorizationException(e.getMessage(),e);
        }

    }

    @Override
    public void createValidationToken(User user, String token, Date expiryDate) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpireDate(expiryDate);

        try {
            tokenRepository.save(verificationToken);
        } catch (RuntimeException e) {
            log.error(e.getMessage(),e);
            throw new GeneralAuthorizationException(e.getMessage(),e);
        }

    }

    @Override
    public User validateToken(String aValidToken) {
        List<VerificationToken> tokenList = tokenRepository.findByToken(aValidToken, new Date());

        if (tokenList.isEmpty()) {
            throw new TokenNotFoundException("Token Not Found");
        }

        VerificationToken verificationToken = tokenList.get(0);
        User user = verificationToken.getUser();
        user.setEnabled(true);

        return userRepository.save(user);
    }
}
