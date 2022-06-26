package com.my.exception;

public class UserServiceException extends Exception {

    public UserServiceException() {
        super("Unknown user");
    }

    public UserServiceException(String message) {
        super(message);
    }
}
