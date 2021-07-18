package com.borio.authorization.domain.exceptions;

public class UserNotFoundException extends GeneralAuthorizationException {

    public UserNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
