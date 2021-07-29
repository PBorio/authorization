package com.borio.authorization.domain.exceptions;

public class ProposalNotFoundException extends GeneralAuthorizationException {
    public ProposalNotFoundException(String message) {
        super(message);
    }

    public ProposalNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
