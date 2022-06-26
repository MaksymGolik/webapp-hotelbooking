package com.my.service;

import com.my.db.entity.*;
import com.my.exception.DataNotFoundException;
import com.my.exception.HotelRoomServiceException;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface IHotelRoomService {
    List<HotelRoom> getAllRoomsThanGroupByInfo() throws HotelRoomServiceException;
    List<HotelRoom> getAllAvailableRoomsByDates(Date checkIn, Date checkOut) throws HotelRoomServiceException;
    List<List<HotelRoom>> getAvailableRoomsByDatesAndNumberOfPeople (Date checkIn, Date checkOut, int numberOfPeople) throws HotelRoomServiceException;
    List<HotelRoom> getAllAvailableRoomsByDatesAndType(Date checkIn, Date checkOut, RoomType.TypeName type) throws HotelRoomServiceException;
    List<List<HotelRoom>> getAvailableRoomsByDatesAndNumberOfPeopleAndType (Date checkIn, Date checkOut, int numberOfPeople, RoomType.TypeName type) throws HotelRoomServiceException;
    HotelRoom getRoomById (long roomId) throws HotelRoomServiceException;
    boolean checkRoomAvailabilityByDates(long roomId, Date checkIn, Date checkOut) throws HotelRoomServiceException;
    Map<RoomInfo,Integer> getTopBookedRooms() throws HotelRoomServiceException;
}
