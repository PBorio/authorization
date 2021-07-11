package com.borio.authorization.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Token Expired")
public class TokenExpiredException extends GeneralAuthorizationException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
