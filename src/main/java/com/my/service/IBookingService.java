package com.my.service;

import com.my.db.entity.Booking;
import com.my.db.entity.BookingItem;
import com.my.db.entity.BookingStatus;
import com.my.exception.BookingServiceException;

import java.util.List;

public interface IBookingService {
    long insertBooking (Booking booking) throws BookingServiceException;
    boolean insertBookingItems (List<BookingItem> items) throws BookingServiceException;
    List<BookingStatus> getStatuses() throws BookingServiceException;
    List<Booking> getBookingsByUserId(long userId) throws BookingServiceException;
    Booking getBookingById (long bookingId) throws BookingServiceException;
    List<BookingItem> getBookingItemsByBookingId (long bookingId) throws BookingServiceException;
    boolean changeBookingStatus (long bookingId, BookingStatus status) throws BookingServiceException;
    List<Booking> getBookingsByStatus (BookingStatus status) throws BookingServiceException;
}
