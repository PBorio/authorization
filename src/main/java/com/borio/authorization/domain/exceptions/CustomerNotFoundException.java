package com.borio.authorization.domain.exceptions;

public class CustomerNotFoundException extends GeneralAuthorizationException {
    public CustomerNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
