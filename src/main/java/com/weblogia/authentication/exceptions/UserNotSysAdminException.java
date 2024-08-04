package com.weblogia.authentication.exceptions;

public class UserNotSysAdminException extends RuntimeException {
    public UserNotSysAdminException(String message) {
        super(message);
    }
}
