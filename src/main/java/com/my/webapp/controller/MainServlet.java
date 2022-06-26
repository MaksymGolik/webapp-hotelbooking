package com.my.webapp.controller;

import com.my.db.entity.HotelRoom;
import com.my.exception.HotelRoomServiceException;
import com.my.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/home")
public class MainServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MainServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<HotelRoom> roomList = ServiceFactory.getHotelRoomService().getAllRoomsThanGroupByInfo();
            req.setAttribute("roomList",roomList);
            req.setAttribute("type","list");
            req.getRequestDispatcher("/jsp-pages/main.jsp").forward(req,resp);
        } catch (HotelRoomServiceException e) {
            log.error(e);
            req.setAttribute("errorMessage","Oops, this hotel doesn't seem to have rooms yet, try again later");
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }
    }
}
