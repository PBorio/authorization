package com.borio.authorization.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")
public class SystemAuthorizationException extends RuntimeException {

    public SystemAuthorizationException(String message, Throwable e) {
        super(message, e);
    }

    public SystemAuthorizationException(String message) {
        super(message);
    }
}
