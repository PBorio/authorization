package com.borio.authorization.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")
public class AdminRoleNotFoundException extends GeneralAuthorizationException {

    public AdminRoleNotFoundException(String message) {
        super(message);
    }
}
