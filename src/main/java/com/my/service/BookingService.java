package com.my.service;

import com.my.db.DAOFactory;
import com.my.db.IBookingDAO;
import com.my.db.TypeDAO;
import com.my.db.entity.Booking;
import com.my.db.entity.BookingItem;
import com.my.db.entity.BookingStatus;
import com.my.exception.BookingServiceException;
import com.my.exception.DBConnectionException;
import com.my.exception.DataNotFoundException;
import com.my.utils.Validator;
import org.apache.log4j.Logger;
import java.util.List;

public class BookingService implements IBookingService {

    private static final Logger log = Logger.getLogger(BookingService.class);

    private static final TypeDAO type = TypeDAO.MySQL;
    private static DAOFactory dao;
    private static IBookingDAO bookingDAO;

    static {
        dao = DAOFactory.getDAOInstance(type);
        bookingDAO = dao.getBookingDAO();
    }

    @Override
    public synchronized long insertBooking(Booking booking) throws BookingServiceException {
        long bookingId = -1;
        try {
            dao.open();
            bookingDAO = dao.getBookingDAO();
            if(!Validator.validateBooking(booking)) throw new BookingServiceException("Incorrect booking data");
            bookingId = bookingDAO.insertBooking(booking);
            if(bookingId==-1) throw new BookingServiceException("An error occurred while creating a booking");
        } catch (DBConnectionException e) {
            log.error(e);
            throw new BookingServiceException("An error occurred while creating a booking");
        } finally {
            dao.close();
        }
        return bookingId;
    }

    @Override
    public synchronized boolean insertBookingItems(List<BookingItem> items) throws BookingServiceException {
        try {
            dao.beginTransaction();
            bookingDAO = dao.getBookingDAO();
            for(BookingItem item : items){
                if(!bookingDAO.insertBookingItem(item)){
                    dao.rollbackTransaction();
                    throw new BookingServiceException("An error occurred while creating a booking");
                }
            }
            dao.commitTransaction();
        } catch (DBConnectionException e) {
            log.error(e);
            try {
                dao.rollbackTransaction();
                throw new BookingServiceException("An error occurred while creating a booking");
            } catch (DBConnectionException ex){
                log.error(ex);
            }
        }
        return true;
    }

    @Override
    public List<BookingStatus> getStatuses() throws BookingServiceException {
        List<BookingStatus> statusList;
        try{
            dao.open();
            bookingDAO = dao.getBookingDAO();
            statusList = bookingDAO.getStatuses();
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new BookingServiceException("Cannot determine booking status");
        }
        return statusList;
    }

    @Override
    public List<Booking> getBookingsByUserId(long userId) throws BookingServiceException {
        List<Booking> bookingList;
        try{
            dao.open();
            bookingDAO = dao.getBookingDAO();
            bookingList = bookingDAO.getBookingsByUserId(userId);
            dao.close();
        } catch (DBConnectionException | DataNotFoundException e) {
            log.error(e);
            throw new BookingServiceException("Cannot find bookings for user");
        }
        return bookingList;
    }

    @Override
    public Booking getBookingById(long bookingId) throws BookingServiceException {
        Booking booking;
        try{
            dao.open();
            bookingDAO = dao.getBookingDAO();
            booking = bookingDAO.getBookingById(bookingId);
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new BookingServiceException("Cannot find booking");
        }
        return booking;
    }

    @Override
    public List<BookingItem> getBookingItemsByBookingId(long bookingId) throws BookingServiceException {
        List<BookingItem> bookingItemList;
        try{
            dao.open();
            bookingDAO = dao.getBookingDAO();
            bookingItemList = bookingDAO.getBookingItemsByBookingId(bookingId);
            dao.close();
        } catch (DBConnectionException | DataNotFoundException e) {
            log.error(e);
            throw new BookingServiceException("There are problems with the content of the booking");
        }
        return bookingItemList;
    }

    @Override
    public synchronized boolean changeBookingStatus(long bookingId, BookingStatus status) throws BookingServiceException {
        try{
            dao.open();
            bookingDAO = dao.getBookingDAO();
            if(!Validator.validateNewStatus(bookingDAO.getBookingById(bookingId).getStatus(),status)){
             throw new BookingServiceException("failed to change the status, perhaps the booking already has a different status");
            }
            if(!bookingDAO.changeBookingStatus(bookingId, status)) {
                throw new BookingServiceException("Unable to change booking status");
            }
        } catch (DBConnectionException e){
            log.error(e);
            throw new BookingServiceException("Unable to change booking status");
        } catch (DataNotFoundException e) {
            log.error(e);
            throw new BookingServiceException("Cannot find booking which status change requested");
        } finally {
            dao.close();
        }
        return true;
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) throws BookingServiceException {
        List<Booking> bookingList;
        try{
            dao.open();
            bookingDAO = dao.getBookingDAO();
            bookingList = bookingDAO.getBookingsByStatus(status);
            dao.close();
        } catch (DBConnectionException | DataNotFoundException e) {
            log.error(e);
            throw new BookingServiceException("Cannot find bookings by status");
        }
        return bookingList;
    }
}
