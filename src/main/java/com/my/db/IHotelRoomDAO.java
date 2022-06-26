package com.my.db;

import com.my.db.entity.HotelRoom;
import com.my.db.entity.RoomInfo;
import com.my.db.entity.RoomType;
import com.my.exception.DataNotFoundException;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface IHotelRoomDAO {
    List<HotelRoom> getAllRoomsThanGroupByInfo() throws DataNotFoundException;
    List<HotelRoom> getAllAvailableRoomsByDates(Date checkIn, Date checkOut) throws DataNotFoundException;
    Map<Integer,List<HotelRoom>> getAllAvailableRoomsByDatesAndNumberOfPeople (Date checkIn, Date checkOut, int numberOfPeople) throws DataNotFoundException;
    List<HotelRoom> getAllAvailableRoomsByDatesAndType(Date checkIn, Date checkOut, RoomType.TypeName type) throws DataNotFoundException;
    Map<Integer,List<HotelRoom>> getAllAvailableRoomsByDatesAndNumberOfPeopleAndType (Date checkIn, Date checkOut, int numberOfPeople, RoomType.TypeName type) throws DataNotFoundException;
    HotelRoom getRoomById (long roomId) throws DataNotFoundException;
    boolean checkRoomAvailabilityByDates(long roomId, Date checkIn, Date checkOut) throws DataNotFoundException;
    Map<RoomInfo,Integer> getTopBookedRooms() throws DataNotFoundException;
}
