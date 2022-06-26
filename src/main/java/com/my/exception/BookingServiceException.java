package com.my.exception;

public class BookingServiceException extends Exception{
    public BookingServiceException(){ super(); }
    public BookingServiceException(String message) {super(message);}
}
