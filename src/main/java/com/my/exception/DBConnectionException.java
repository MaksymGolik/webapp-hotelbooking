package com.my.exception;

public class DBConnectionException extends Exception{
        public DBConnectionException() {
            super("Unable to connect to database");
        }
        public DBConnectionException(String message) {
            super(message);
        }
}
