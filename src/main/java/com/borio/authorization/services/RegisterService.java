package com.borio.authorization.services;

import com.borio.authorization.domain.User;

import java.util.Date;

public interface RegisterService {

    User save(User user);

    void createValidationToken(User user, String token, Date expiryDate);

    User validateToken(String aValidToken);
}
