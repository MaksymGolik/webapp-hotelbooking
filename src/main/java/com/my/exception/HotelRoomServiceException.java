package com.my.exception;

public class HotelRoomServiceException extends Exception{
    public HotelRoomServiceException(){ super(); }
    public HotelRoomServiceException(String message) {super(message);}
}
