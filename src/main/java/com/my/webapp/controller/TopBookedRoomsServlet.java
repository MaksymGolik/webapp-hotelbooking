package com.my.webapp.controller;

import com.my.db.entity.RoomInfo;
import com.my.exception.HotelRoomServiceException;
import com.my.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/top")
public class TopBookedRoomsServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(TopBookedRoomsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<RoomInfo, Integer> top = ServiceFactory.getHotelRoomService().getTopBookedRooms();
            req.setAttribute("top",top);
            req.getRequestDispatcher("/jsp-pages/topBookedRooms.jsp").forward(req,resp);
        } catch (HotelRoomServiceException e) {
            log.error(e);
            req.setAttribute("errorMessage",e.getMessage());
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }
    }
}
