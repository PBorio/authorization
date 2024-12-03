package com.weblogia.authentication.exceptions;

public class PermissionInvalidException extends  RuntimeException {

    public PermissionInvalidException(String message){
        super(message);
    }
}
