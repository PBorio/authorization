package com.borio.authorization.domain.exceptions;

public class CompanyNotFoundException extends GeneralAuthorizationException {
    public CompanyNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
