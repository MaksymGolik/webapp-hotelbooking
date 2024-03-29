package com.my.db;

import com.my.db.entity.Booking;
import com.my.db.entity.BookingItem;
import com.my.db.entity.BookingStatus;
import com.my.db.entity.HotelRoom;
import com.my.exception.DataNotFoundException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLBookingDAOImpl implements IBookingDAO{

    private static final Logger log = Logger.getLogger(MySQLBookingDAOImpl.class);

    private Connection connection;

    public MySQLBookingDAOImpl(Connection connection) {
        this.connection = connection;
    }

    private static final String INSERT_BOOKING = "INSERT INTO booking (guest_surname, " +
            " guest_name, guest_phone_number, create_time, last_update_time, user_id, status_id)" +
            " VALUES (?,?,?,now(),now(),?,?)";
    private static final String INSERT_BOOKING_ITEM = "INSERT INTO booking_has_hotel_room (booking_id, hotel_room_id," +
            " checkIn_date, checkOut_date) VALUES (?,?,?,?)";
    private static final String GET_ALL_STATUSES = "SELECT * FROM booking_status";
    private static final String GET_ID_LAST_CREATED_BOOKING_BY_USER_ID = "SELECT id FROM booking WHERE user_id=? ORDER BY create_time DESC LIMIT 1";
    private static final String GET_BOOKINGS_BY_USER_ID = "SELECT booking.id, guest_surname, guest_name, " +
            "guest_phone_number ,booking.price, create_time, last_update_time, user_id, status_id,name,description " +
            "FROM booking JOIN booking_status ON status_id=booking_status.id WHERE user_id=?  ORDER BY create_time DESC";
    private static final String GET_BOOKING_BY_ID = "SELECT booking.id, guest_surname, guest_name, " +
            "guest_phone_number ,booking.price, create_time, last_update_time, user_id, status_id,name, description " +
            "FROM booking JOIN booking_status ON status_id=booking_status.id WHERE booking.id=?";
    private static final String GET_BOOKING_ITEMS_BY_BOOKING_ID = "SELECT * FROM booking_has_hotel_room WHERE booking_id = ?";
    private static final String SET_BOOKING_STATUS = "UPDATE booking SET status_id = ? WHERE id = ?";
    private static final String GET_BOOKINGS_BY_STATUS = "SELECT booking.id, guest_surname, guest_name, " +
            "guest_phone_number ,booking.price, create_time, last_update_time, user_id, status_id,name, description " +
            "FROM booking JOIN booking_status ON status_id=booking_status.id WHERE status_id=?  ORDER BY create_time DESC";

    private Booking extractBooking(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
            booking.setId(resultSet.getLong(1));
            booking.setGuestSurname(resultSet.getString(2));
            booking.setGuestName(resultSet.getString(3));
            booking.setGuestPhoneNumber(resultSet.getString(4));
            booking.setPrice(resultSet.getDouble(5));
            booking.setCreateTime(Timestamp.valueOf(resultSet.getString(6)));
            booking.setLastUpdateTime(Timestamp.valueOf(resultSet.getString(7)));
            booking.setUserId(resultSet.getLong(8));
            BookingStatus status = new BookingStatus();
            status.setId(resultSet.getLong(9));
            status.setName(BookingStatus.StatusName.valueOf(resultSet.getString(10)));
            status.setDescription(resultSet.getString(11));
            booking.setStatus(status);
        return booking;
    }

    private BookingItem extractBookingItem(ResultSet resultSet) throws SQLException {
        BookingItem item = new BookingItem();
        item.setBookingId(resultSet.getLong(1));
        HotelRoom room = new HotelRoom();
        room.setId(resultSet.getLong(2));
        item.setRoom(room);
        item.setCheckInDate(Date.valueOf(resultSet.getString(3)));
        item.setCheckOutDate(Date.valueOf(resultSet.getString(4)));
        item.setPrice(resultSet.getDouble(5));
        return item;
    }

    @Override
    public long insertBooking(Booking booking) {
        long newId = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOKING, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, booking.getGuestSurname());
            preparedStatement.setString(2, booking.getGuestName());
            preparedStatement.setString(3, booking.getGuestPhoneNumber());
            preparedStatement.setLong(4,booking.getUserId());
            preparedStatement.setLong(5,booking.getStatus().getId());
            preparedStatement.executeUpdate();
            ResultSet key = preparedStatement.getGeneratedKeys();
            if(key.next()) newId = key.getLong(1);
            MySQLDAOFactory.close(preparedStatement);
        } catch (SQLException e){
            log.error(e);
        }
        return newId;
    }

    @Override
    public boolean insertBookingItem(BookingItem item) {
        int affectedRows = -1;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOKING_ITEM);
            preparedStatement.setLong(1,item.getBookingId());
            preparedStatement.setLong(2,item.getRoom().getId());
            preparedStatement.setString(3,item.getCheckInDate().toString());
            preparedStatement.setString(4,item.getCheckOutDate().toString());
            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e){
            log.error(e);
        }
        return affectedRows == 1;
    }

    @Override
    public List<BookingStatus> getStatuses() throws DataNotFoundException {
        List<BookingStatus> statusList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_STATUSES);
            while (resultSet.next()) {
                BookingStatus status = new BookingStatus();
                status.setId(resultSet.getLong(1));
                status.setName(BookingStatus.StatusName.valueOf(resultSet.getString(2).toUpperCase()));
                status.setDescription(resultSet.getString(3));
                statusList.add(status);
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(statement);
            if(statusList.size()==0) throw new DataNotFoundException();
        }
        catch (SQLException e){
            log.error(e);
            throw new DataNotFoundException();
        }
        return statusList;
    }

    @Override
    public List<Booking> getBookingsByUserId(long userId) throws DataNotFoundException {
        List<Booking> bookingList = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKINGS_BY_USER_ID);
            preparedStatement.setLong(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                bookingList.add(extractBooking(resultSet));
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
            if(bookingList.size()==0)throw new DataNotFoundException();
        }catch (SQLException e){
            log.error(e);
            throw new DataNotFoundException();
        }
        return bookingList;
    }

    public Booking getBookingById(long bookingId) throws DataNotFoundException {
        Booking booking = new Booking();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(GET_BOOKING_BY_ID);
            preparedStatement.setLong(1, bookingId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                booking = extractBooking(resultSet);
            } else throw new DataNotFoundException();
        } catch (SQLException e) {
            log.error(e);
            throw new DataNotFoundException();
        } finally {
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
        }
        return booking;
    }

    @Override
    public List<BookingItem> getBookingItemsByBookingId(long bookingId) throws DataNotFoundException {
        List<BookingItem> bookingItems = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKING_ITEMS_BY_BOOKING_ID);
            preparedStatement.setLong(1,bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                bookingItems.add(extractBookingItem(resultSet));
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
            if(bookingItems.size()==0) throw new DataNotFoundException();
        }catch (SQLException e){
            log.error(e);
            throw new DataNotFoundException();
        }
        return bookingItems;
    }

    @Override
    public boolean changeBookingStatus(long bookingId, BookingStatus status) {
        int affectedRows = -1;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(SET_BOOKING_STATUS);
            preparedStatement.setLong(1,status.getId());
            preparedStatement.setLong(2,bookingId);
            affectedRows = preparedStatement.executeUpdate();
            MySQLDAOFactory.close(preparedStatement);
        }catch (SQLException e){
            log.error(e);
        }
        return affectedRows == 1;
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) throws DataNotFoundException {
        List<Booking> bookingList = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKINGS_BY_STATUS);
            preparedStatement.setLong(1,status.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                bookingList.add(extractBooking(resultSet));
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
            if(bookingList.size()==0)throw new DataNotFoundException();
        }catch (SQLException e){
            log.error(e);
            throw new DataNotFoundException();
        }
        return bookingList;
    }
}
