package com.borio.authorization.config.multinenancy.client.per.database.exceptions;

import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;

public class TenantNotFoundException extends GeneralAuthorizationException {
    public TenantNotFoundException(String message) {
        super(message);
    }
}
