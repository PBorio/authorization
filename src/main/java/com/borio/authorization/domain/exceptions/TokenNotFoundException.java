package com.borio.authorization.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Token Not Found")
public class TokenNotFoundException extends GeneralAuthorizationException {

    public TokenNotFoundException(String message) {
        super(message);
    }
}
