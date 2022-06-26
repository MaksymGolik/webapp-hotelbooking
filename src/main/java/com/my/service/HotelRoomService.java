package com.my.service;

import com.my.db.DAOFactory;
import com.my.db.IHotelRoomDAO;
import com.my.db.TypeDAO;
import com.my.db.entity.HotelRoom;
import com.my.db.entity.RoomInfo;
import com.my.db.entity.RoomType;
import com.my.exception.DBConnectionException;
import com.my.exception.DataNotFoundException;
import com.my.exception.HotelRoomServiceException;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class HotelRoomService implements IHotelRoomService{

    private static final Logger log = Logger.getLogger(HotelRoomService.class);

    private static final TypeDAO type = TypeDAO.MySQL;
    private static DAOFactory dao;
    private static IHotelRoomDAO hotelRoomDAO;

    static {
        dao = DAOFactory.getDAOInstance(type);
        hotelRoomDAO = dao.getHotelRoomDAO();
    }

    @Override
    public List<HotelRoom> getAllRoomsThanGroupByInfo() throws HotelRoomServiceException {
        List<HotelRoom> roomList;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            roomList = hotelRoomDAO.getAllRoomsThanGroupByInfo();
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("No data on the specification request");
        }
        return roomList;
    }

    @Override
    public List<HotelRoom> getAllAvailableRoomsByDates(Date checkIn, Date checkOut) throws HotelRoomServiceException {
        List<HotelRoom> roomList;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            roomList = hotelRoomDAO.getAllAvailableRoomsByDates(checkIn, checkOut);
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("According to the given criteria no available rooms were found");
        }
        return roomList;
    }

    @Override
    public List<HotelRoom> getAllAvailableRoomsByDatesAndType(Date checkIn, Date checkOut, RoomType.TypeName type) throws HotelRoomServiceException {
        List<HotelRoom> roomList;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            roomList = hotelRoomDAO.getAllAvailableRoomsByDatesAndType(checkIn, checkOut,type);
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("According to the given criteria no available rooms were found");
        }
        return roomList;
    }

    @Override
    public List<List<HotelRoom>> getAvailableRoomsByDatesAndNumberOfPeopleAndType(Date checkIn, Date checkOut, int numberOfPeople, RoomType.TypeName type) throws HotelRoomServiceException {
        List<List<HotelRoom>> roomListing;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            Map<Integer, List<HotelRoom>> roomMap;
            roomMap = hotelRoomDAO.getAllAvailableRoomsByDatesAndNumberOfPeopleAndType(checkIn, checkOut, numberOfPeople,type);
            List<Map<Object,Long>> ways
                    = combinationSettlement(new ArrayList<>(roomMap.keySet()), numberOfPeople);

            roomListing = groupRoomForSettlementByWays(ways,roomMap);

            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("According to the given criteria no available rooms were found");
        }
        return roomListing;
    }

    @Override
    public HotelRoom getRoomById(long roomId) throws HotelRoomServiceException {
        HotelRoom room;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            room = hotelRoomDAO.getRoomById(roomId);
            dao.close();
        } catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("Hotel room not found");
        }
        return room;
    }

    @Override
    public boolean checkRoomAvailabilityByDates(long roomId, Date checkIn, Date checkOut) throws HotelRoomServiceException {
        boolean result = false;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            result = hotelRoomDAO.checkRoomAvailabilityByDates(roomId, checkIn, checkOut);
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("Failed to check the room for availability");
        }
        return result;
    }

    @Override
    public Map<RoomInfo, Integer> getTopBookedRooms() throws HotelRoomServiceException {
        Map<RoomInfo, Integer> top;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            top = hotelRoomDAO.getTopBookedRooms();
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("No rooms in the booking TOP");
        }
        return top;
    }

    @Override
    public List<List<HotelRoom>> getAvailableRoomsByDatesAndNumberOfPeople(Date checkIn, Date checkOut, int numberOfPeople) throws HotelRoomServiceException {
        List<List<HotelRoom>> roomListing;
        try{
            dao.open();
            hotelRoomDAO = dao.getHotelRoomDAO();
            Map<Integer, List<HotelRoom>> roomMap;
            roomMap = hotelRoomDAO.getAllAvailableRoomsByDatesAndNumberOfPeople(checkIn, checkOut, numberOfPeople);
            List<Map<Object,Long>> ways
                    = combinationSettlement(new ArrayList<>(roomMap.keySet()), numberOfPeople);

            roomListing = groupRoomForSettlementByWays(ways,roomMap);

            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new HotelRoomServiceException("According to the given criteria no available rooms were found");
        }
        return roomListing;
    }

    private static List<Map<Object,Long>>
    combinationSettlement(ArrayList<Integer> arr, int sum) throws DataNotFoundException {
        ArrayList<ArrayList<Integer> > ans
                = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();

        // first do hashing since hashset does not always
        // sort
        //  removing the duplicates using HashSet and
        // Sorting the arrayList

        Set<Integer> set = new HashSet<>(arr);
        arr.clear();
        arr.addAll(set);
        Collections.sort(arr);

        findNumbers(ans, arr, sum, 0, temp);
        List<Map<Object,Long>> list = new ArrayList<>();
        ans.forEach(arrayList -> {
            list.add(arrayList.stream().collect(Collectors.groupingBy(k->k,Collectors.counting())));
        });
        if(list.size()==0)throw new DataNotFoundException("There is no way to accommodate the specified number of people in the available rooms");
        Collections.reverse(list);
        return list;
    }

    private static void
    findNumbers(ArrayList<ArrayList<Integer> > ans,
                ArrayList<Integer> arr, int sum, int index,
                ArrayList<Integer> temp)
    {

        if (sum == 0) {

            // Adding deep copy of list to ans

            ans.add(new ArrayList<>(temp));
            return;
        }

        for (int i = index; i < arr.size(); i++) {

            // checking that sum does not become negative

            if ((sum - arr.get(i)) >= 0) {

                // adding element which can contribute to
                // sum

                temp.add(arr.get(i));

                findNumbers(ans, arr, sum - arr.get(i), i,
                        temp);

                // removing element from list (backtracking)
                temp.remove(arr.get(i));
            }
        }
    }

    private static List<List<HotelRoom>> groupRoomForSettlementByWays(List<Map<Object,Long>> ways,Map<Integer, List<HotelRoom>> roomMap){
        List<List<HotelRoom>> roomListing = new ArrayList<>();

        for(Map<Object,Long> map : ways) {
            List<HotelRoom> position = new ArrayList<>();
            boolean flag = true;
            for (Map.Entry<Object, Long> entry : map.entrySet()) {
                if (roomMap.get(entry.getKey()).size() >= entry.getValue()) {
                    for (int i = 0; i < entry.getValue(); i++) {
                        position.add(roomMap.get(entry.getKey()).get(i));
                    }
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                roomListing.add(position);
            }
            //flag = true;
        }
        return roomListing;
    }
}
