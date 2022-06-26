package com.my.service;

public class ServiceFactory {
    public static IUserService getUserService(){
        return new UserService();
    }
    public static IHotelRoomService getHotelRoomService() { return new HotelRoomService(); }
    public static IBookingService getBookingService() { return new BookingService(); }
}
