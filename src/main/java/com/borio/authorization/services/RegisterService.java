package com.borio.authorization.services;

import com.borio.authorization.domain.User;

public interface RegisterService {

    User save(User convert);

    void sendMail(User user);

    void createValidationToken(User user, String token);

    void validateToken(String aValidToken);
}
