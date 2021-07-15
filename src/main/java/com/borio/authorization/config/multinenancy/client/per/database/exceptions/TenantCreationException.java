package com.borio.authorization.config.multinenancy.client.per.database.exceptions;

import com.borio.authorization.domain.exceptions.GeneralAuthorizationException;
import org.springframework.dao.DataAccessException;

public class TenantCreationException extends GeneralAuthorizationException {
    public TenantCreationException(String message) {
        super(message);
    }

    public TenantCreationException(String message, Throwable e) {
        super(message,e);
    }
}
