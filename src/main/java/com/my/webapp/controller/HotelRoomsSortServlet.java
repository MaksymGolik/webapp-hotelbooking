package com.my.webapp.controller;

import com.my.db.entity.HotelRoom;
import com.my.db.entity.RoomInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/sortRooms")
public class HotelRoomsSortServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<HotelRoom> roomList = null;
        List<Map<RoomInfo,List<Long>>> listRoomMap = null;
        if(req.getParameter("type").equals("list")){
            roomList = (List<HotelRoom>) req.getSession().getAttribute("roomList");
            req.getSession().removeAttribute("roomList");
        }
        if(req.getParameter("type").equals("listMap")){
            listRoomMap = (List<Map<RoomInfo, List<Long>>>) req.getSession().getAttribute("listRoomMap");
            req.getSession().removeAttribute("listRoomMap");
        }

        if(req.getParameter("typeSort").equals("capacitySort")){
            if(roomList!=null){
                if(req.getParameter("capacitySort")==null){
                    roomList.sort(Comparator.comparingInt(o -> o.getInfo().getCapacity()*(-1)));
                } else{
                    roomList.sort(Comparator.comparingInt(o -> o.getInfo().getCapacity()));
                }
            } else{
                Map<RoomInfo,List<Long>> sortedMapByCapacity;
                if(req.getParameter("capacitySort")==null){
                    sortedMapByCapacity = new TreeMap<>(Comparator.comparingInt(o -> o.getCapacity()*(-1)));
                } else{
                    sortedMapByCapacity = new TreeMap<>(Comparator.comparingInt(RoomInfo::getCapacity));
                }
                    for(Map<RoomInfo,List<Long>> map : listRoomMap){
                        sortedMapByCapacity.putAll(map);
                        listRoomMap.set(listRoomMap.indexOf(map),new LinkedHashMap<>(sortedMapByCapacity));
                        sortedMapByCapacity.clear();
                    }
            }
        }
        if(req.getParameter("typeSort").equals("priceSort")){
            if(roomList!=null){
                if(req.getParameter("priceSort")==null){
                    roomList.sort(Comparator.comparingDouble(o -> o.getInfo().getPrice()*(-1)));
                } else{
                    roomList.sort(Comparator.comparingDouble(o -> o.getInfo().getPrice()));
                }
            } else {
                Map<RoomInfo,List<Long>> sortedMapByPrice;
                if(req.getParameter("priceSort")==null){
                    sortedMapByPrice = new TreeMap<>(Comparator.comparingDouble(o -> o.getPrice()*(-1)));
                } else{
                    sortedMapByPrice = new TreeMap<>(Comparator.comparingDouble(RoomInfo::getPrice));
                }
                    for(Map<RoomInfo,List<Long>> map : listRoomMap){
                        sortedMapByPrice.putAll(map);
                        listRoomMap.set(listRoomMap.indexOf(map),new LinkedHashMap<>(sortedMapByPrice));
                        sortedMapByPrice.clear();
                    }
            }
        }

        if(req.getParameter("availableForBooking")!=null){
            req.setAttribute("availableForBooking",true);
        }
        if(roomList!=null){
            req.setAttribute("type","list");
            req.setAttribute("roomList",roomList);
        } else{
            req.setAttribute("type","listMap");
            req.setAttribute("listRoomMap",listRoomMap);
        }
        req.getRequestDispatcher("/jsp-pages/main.jsp").forward(req,resp);
    }
}
