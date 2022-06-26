package com.my.db;

import com.my.db.entity.Booking;
import com.my.db.entity.BookingItem;
import com.my.db.entity.BookingStatus;
import com.my.exception.DataNotFoundException;

import java.util.List;
import java.util.Map;

public interface IBookingDAO {
    long insertBooking (Booking booking);
    boolean insertBookingItem(BookingItem item);
    List<BookingStatus> getStatuses () throws DataNotFoundException;
    List<Booking> getBookingsByUserId (long userId) throws DataNotFoundException;
    Booking getBookingById (long bookingId) throws DataNotFoundException;
    List<BookingItem> getBookingItemsByBookingId (long bookingId) throws DataNotFoundException;
    boolean changeBookingStatus (long bookingId, BookingStatus status);
    List<Booking> getBookingsByStatus (BookingStatus status) throws DataNotFoundException;
}
