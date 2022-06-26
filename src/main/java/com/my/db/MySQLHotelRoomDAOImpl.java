package com.my.db;

import com.my.db.entity.HotelRoom;
import com.my.db.entity.RoomInfo;
import com.my.db.entity.RoomType;
import com.my.exception.DataNotFoundException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySQLHotelRoomDAOImpl implements IHotelRoomDAO{

    private static final Logger log = Logger.getLogger(MySQLHotelRoomDAOImpl.class);

    private Connection connection;

    public MySQLHotelRoomDAOImpl(Connection connection) {
        this.connection = connection;
    }

    private static final String GET_ALL_ROOMS_GROUP_BY_ROOM_INFO = "SELECT hotel_room.id, room_info.id, capacity, price, room_info.description, image_url, room_type.id, name,room_type.description FROM hotel_room JOIN room_info on room_info_id=room_info.id JOIN room_type ON type_id=room_type.id GROUP BY room_info.id";
    private static final String CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES = "CALL searchAvailableRoomsByDates (?,?)";
    private static final String CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES_AND_NUMBER_OF_PEOPLE = "CALL searchAvailableRoomsByDatesAndNumberOfPeople (?,?,?)";
    private static final String CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES_AND_ROOM_TYPE = "CALL searchAvailableRoomsByDatesAndRoomType (?,?,?)";
    private static final String CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES_AND_NUMBER_OF_PEOPLE_AND_ROOM_TYPE = "CALL searchAvailableRoomsByDatesAndNumberOfPeopleAndRoomType (?,?,?,?)";
    private static final String GET_ROOM_BY_ID = "SELECT hotel_room.id, room_info.id, capacity, price, room_info.description, image_url, room_type.id, name,room_type.description FROM hotel_room JOIN room_info on room_info_id=room_info.id JOIN room_type ON type_id=room_type.id WHERE hotel_room.id=?";
    private static final String CHECK_ROOM_AVAILABILITY_BY_DATES = "SELECT DISTINCT hotel_room_id FROM booking_has_hotel_room JOIN booking on booking_id=id" +
            " WHERE (checkOut_date BETWEEN (?+INTERVAL 1 DAY) AND ? OR checkIn_date BETWEEN ? AND (?-INTERVAL 1 DAY))AND booking.status_id NOT IN (2,4) AND hotel_room_id=?";
    private static final String GET_TOP_BOOKED_ROOMS =
            "SELECT COUNT(hotel_room_id) as COUNTER, room_info_id,capacity,room_info.price,room_info.description,image_url, type_id, room_type.name, room_type.description " +
            "FROM booking_has_hotel_room JOIN booking on booking_id = booking.id " +
            "JOIN booking_status ON status_id = booking_status.id " +
            "JOIN hotel_room ON hotel_room_id = hotel_room.id " +
            "JOIN room_info ON room_info_id=room_info.id " +
            "JOIN room_type ON type_id=room_type.id WHERE booking_status.name = 'COMPLETED' GROUP BY room_info_id ORDER BY COUNTER DESC";

    private HotelRoom extractRoom(ResultSet resultSet){
        HotelRoom room = new HotelRoom();
        try {
            room.setId(resultSet.getLong(1));
            RoomInfo info = new RoomInfo();
            info.setId(resultSet.getLong(2));
            info.setCapacity(resultSet.getInt(3));
            info.setPrice(resultSet.getDouble(4));
            info.setDescription(resultSet.getString(5));
            info.setImageUrl(resultSet.getString(6));
            RoomType type = new RoomType();
            type.setId(resultSet.getLong(7));
            type.setName(RoomType.TypeName.valueOf(resultSet.getString(8).toUpperCase()));
            type.setDescription(resultSet.getString(9));
            info.setType(type);
            room.setInfo(info);
        } catch (SQLException ex){
            log.error(ex);
        }
        return room;
    }

    @Override
    public List<HotelRoom> getAllRoomsThanGroupByInfo() throws DataNotFoundException {
                List<HotelRoom> hotelRoomList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_ROOMS_GROUP_BY_ROOM_INFO);
            while (resultSet.next()) {
                HotelRoom room = extractRoom(resultSet);
                hotelRoomList.add(room);
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(statement);
            if(hotelRoomList.size()==0) throw new DataNotFoundException();
        }
        catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return hotelRoomList;
    }

    @Override
    public List<HotelRoom> getAllAvailableRoomsByDates(Date checkIn, Date checkOut) throws DataNotFoundException {
        List<HotelRoom> hotelRoomList = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES);
            callableStatement.setString(1,checkIn.toString());
            callableStatement.setString(2, checkOut.toString());
            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()){
                HotelRoom room = extractRoom(resultSet);
                hotelRoomList.add(room);
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(callableStatement);
            if(hotelRoomList.size()==0) throw new DataNotFoundException();
        }
        catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return hotelRoomList;
    }

    @Override
    public Map<Integer, List<HotelRoom>> getAllAvailableRoomsByDatesAndNumberOfPeople(Date checkIn, Date checkOut, int numberOfPeople) throws DataNotFoundException {
        Map<Integer,List<HotelRoom>> hotelRoomMap = new LinkedHashMap<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES_AND_NUMBER_OF_PEOPLE);
            callableStatement.setString(1,checkIn.toString());
            callableStatement.setString(2, checkOut.toString());
            callableStatement.setInt(3,numberOfPeople);
            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()){
                HotelRoom room = extractRoom(resultSet);
                if(!hotelRoomMap.containsKey(room.getInfo().getCapacity())){
                    List<HotelRoom> list = new ArrayList<>();
                    list.add(room);
                    hotelRoomMap.put(room.getInfo().getCapacity(),list);
                } else{
                    hotelRoomMap.get(room.getInfo().getCapacity()).add(room);
                }
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(callableStatement);
            if(hotelRoomMap.size()==0)throw new DataNotFoundException();
        }
        catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return hotelRoomMap;
    }

    @Override
    public List<HotelRoom> getAllAvailableRoomsByDatesAndType(Date checkIn, Date checkOut, RoomType.TypeName type) throws DataNotFoundException {
        List<HotelRoom> hotelRoomList = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES_AND_ROOM_TYPE);
            callableStatement.setString(1,checkIn.toString());
            callableStatement.setString(2, checkOut.toString());
            callableStatement.setString(3, type.name());
            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()){
                HotelRoom room = extractRoom(resultSet);
                hotelRoomList.add(room);
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(callableStatement);
            if(hotelRoomList.size()==0)throw new DataNotFoundException();
        }
        catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return hotelRoomList;
    }

    @Override
    public Map<Integer, List<HotelRoom>> getAllAvailableRoomsByDatesAndNumberOfPeopleAndType(Date checkIn, Date checkOut, int numberOfPeople, RoomType.TypeName type) throws DataNotFoundException {
        Map<Integer,List<HotelRoom>> hotelRoomMap = new LinkedHashMap<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(CALL_PROCEDURE_GET_ALL_AVAILABLE_ROOMS_BY_DATES_AND_NUMBER_OF_PEOPLE_AND_ROOM_TYPE);
            callableStatement.setString(1,checkIn.toString());
            callableStatement.setString(2, checkOut.toString());
            callableStatement.setInt(3,numberOfPeople);
            callableStatement.setString(4,type.name());
            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()){
                HotelRoom room = extractRoom(resultSet);
                if(!hotelRoomMap.containsKey(room.getInfo().getCapacity())){
                    List<HotelRoom> list = new ArrayList<>();
                    list.add(room);
                    hotelRoomMap.put(room.getInfo().getCapacity(),list);
                } else{
                    hotelRoomMap.get(room.getInfo().getCapacity()).add(room);
                }
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(callableStatement);
            if(hotelRoomMap.size()==0) throw new DataNotFoundException();
        }
        catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return hotelRoomMap;
    }

    @Override
    public HotelRoom getRoomById(long roomId) throws DataNotFoundException {
        HotelRoom room;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(GET_ROOM_BY_ID);
            preparedStatement.setLong(1, roomId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                room = extractRoom(resultSet);
            } else {
                throw new DataNotFoundException();
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new DataNotFoundException();
        } finally {
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
        }
        return room;
    }

    @Override
    public boolean checkRoomAvailabilityByDates(long roomId, Date checkIn, Date checkOut) throws DataNotFoundException {
        long id = -1;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(CHECK_ROOM_AVAILABILITY_BY_DATES);
            preparedStatement.setString(1, checkIn.toString());
            preparedStatement.setString(3, checkIn.toString());
            preparedStatement.setString(2, checkOut.toString());
            preparedStatement.setString(4, checkOut.toString());
            preparedStatement.setLong(5, roomId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new DataNotFoundException();
        } finally {
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
        }
        return !(id == roomId);
    }

    @Override
    public Map<RoomInfo, Integer> getTopBookedRooms() throws DataNotFoundException {
        Map<RoomInfo,Integer> top = new LinkedHashMap<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_TOP_BOOKED_ROOMS);
            while(resultSet.next()){
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setId(resultSet.getLong(2));
                roomInfo.setCapacity(resultSet.getInt(3));
                roomInfo.setPrice(resultSet.getDouble(4));
                roomInfo.setDescription(resultSet.getString(5));
                roomInfo.setImageUrl(resultSet.getString(6));
                RoomType type = new RoomType();
                type.setId(resultSet.getLong(7));
                type.setName(RoomType.TypeName.valueOf(resultSet.getString(8).toUpperCase()));
                type.setDescription(resultSet.getString(9));
                roomInfo.setType(type);
                top.put(roomInfo, resultSet.getInt(1));
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(statement);
            if(top.size()==0)throw new DataNotFoundException();
        } catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return top;
    }
}
