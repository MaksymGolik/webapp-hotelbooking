package com.my.webapp.controller;

import com.my.db.entity.HotelRoom;
import com.my.db.entity.RoomInfo;
import com.my.db.entity.RoomType;
import com.my.exception.HotelRoomServiceException;
import com.my.utils.Validator;
import com.my.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@WebServlet("/searchRooms")
public class SearchAvailableRoomsServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SearchAvailableRoomsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(!Validator.validatePeriod(req.getParameter("period"))){
            req.setAttribute("errorMessage","Incorrect period specified");
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }

        final String[] period = req.getParameter("period").split(":");
        Date dateFrom = Date.valueOf(period[0].trim());
        Date dateTo = Date.valueOf(period[1].trim());
        System.out.println(req.getParameter("roomType"));
        try {
        if (req.getParameter("numberOfPeople").isEmpty()) {
            List<HotelRoom> roomList = new ArrayList<>();
            if (req.getParameter("roomType").equals("notSelected")) {

                roomList = ServiceFactory.getHotelRoomService()
                        .getAllAvailableRoomsByDates(dateFrom, dateTo);
            } else {
                roomList = ServiceFactory.getHotelRoomService().
                        getAllAvailableRoomsByDatesAndType(dateFrom, dateTo, RoomType.TypeName.valueOf(req.getParameter("roomType")));
            }
            req.setAttribute("roomList", roomList);
            req.setAttribute("type", "list");
        } else {
            List<List<HotelRoom>> roomListing = new ArrayList<>();
            int numberOfPeople = Integer.parseInt(req.getParameter("numberOfPeople"));
            if (req.getParameter("roomType").equals("notSelected")) {


                roomListing = ServiceFactory.getHotelRoomService()
                        .getAvailableRoomsByDatesAndNumberOfPeople(dateFrom, dateTo, numberOfPeople);
            } else {
                roomListing = ServiceFactory.getHotelRoomService().
                        getAvailableRoomsByDatesAndNumberOfPeopleAndType(dateFrom, dateTo, numberOfPeople, RoomType.TypeName.valueOf(req.getParameter("roomType")));
            }
            List<Map<RoomInfo, Integer>> listRoomMap = new ArrayList<>();
            List<String> listId = new ArrayList<>();
            for (List<HotelRoom> list : roomListing) {
                Map<RoomInfo, Integer> map = new LinkedHashMap<>();
                StringBuilder stringBuilder = new StringBuilder();
                for (HotelRoom hotelRoom : list) {
                    if (!map.containsKey(hotelRoom.getInfo())) {
                        map.put(hotelRoom.getInfo(), 1);
                        stringBuilder.append(hotelRoom.getId() + " ");
                    } else {
                        stringBuilder.append(hotelRoom.getId() + " ");
                        map.replace(hotelRoom.getInfo(), map.get(hotelRoom.getInfo()) + 1);
                    }
                }
                listRoomMap.add(map);
                listId.add(new String(stringBuilder).trim());
            }
            req.setAttribute("type", "listMap");
            req.setAttribute("listId", listId);
            req.setAttribute("listRoomMap", listRoomMap);
        }
    } catch (HotelRoomServiceException e){
            log.warn(e);
            req.setAttribute("errorMessage",e.getMessage());
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }
        req.setAttribute("period", req.getParameter("period"));
        req.setAttribute("availableForBooking",true);
        req.getRequestDispatcher("/jsp-pages/main.jsp").forward(req,resp);
    }
}
