package com.weblogia.authentication.exceptions;

public class UserInvalidException extends RuntimeException {
    public UserInvalidException(String message) {
        super(message);
    }
}
