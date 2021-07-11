package com.borio.authorization.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad Request")
public class GeneralAuthorizationException extends RuntimeException {

    public GeneralAuthorizationException(String message) {
        super(message);
    }

}
