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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {


    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    private RoleRepository roleRepository;

    @Autowired
    public RegisterServiceImpl(UserRepository userRepository,
                               TokenRepository tokenRepository,
                               RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User save(User user) {

        Optional<User> oUser = userRepository.findByEmail(user.getEmail());
        Optional<Role> oRole = roleRepository.findByName(SecurityConstants.COMPANY_ADMIN_ROLE);

        if (oRole.isEmpty()) {
            throw new AdminRoleNotFoundException("Email is already registered");
        }

        if (oUser.isPresent()) {
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }

        try {
            user.getRoles().add(oRole.get());
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
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
