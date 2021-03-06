package com.borio.authorization.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Email already registered")
public class EmailAlreadyRegisteredException extends GeneralAuthorizationException {

    public EmailAlreadyRegisteredException(String message, Throwable e) {
        super(message,e);
    }

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
