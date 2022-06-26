package com.my.webapp.controller;

import com.my.db.entity.BookingItem;
import com.my.db.entity.HotelRoom;
import com.my.exception.BookingServiceException;
import com.my.exception.HotelRoomServiceException;
import com.my.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/bookingDetails")
public class BookingDetailsServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(BookingDetailsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("booking", ServiceFactory.getBookingService().getBookingById(Long.parseLong(req.getParameter("bookingId"))));
            List<BookingItem> bookingItems = ServiceFactory.getBookingService().getBookingItemsByBookingId(Long.parseLong(req.getParameter("bookingId")));
            for (BookingItem item : bookingItems) {
                HotelRoom bookedRoom = ServiceFactory.getHotelRoomService().getRoomById(item.getRoom().getId());
                item.setRoom(bookedRoom);
            }
            req.setAttribute("bookingItems", bookingItems);
            req.getRequestDispatcher("/jsp-pages/bookingDetails.jsp").forward(req, resp);
        } catch (HotelRoomServiceException | BookingServiceException e){
            log.error(e);
            req.setAttribute("errorMessage","There are problems with the contents of a booking ("+ e.getMessage()+")");
        }
    }
}
