package com.my.webapp.controller;

import com.my.db.entity.Booking;
import com.my.db.entity.BookingItem;
import com.my.db.entity.HotelRoom;
import com.my.exception.BookingServiceException;
import com.my.exception.HotelRoomServiceException;
import com.my.exception.UserServiceException;
import com.my.service.ServiceFactory;
import com.mysql.cj.log.Log;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/bookingControl")
public class BookingControlServlet extends HttpServlet {

   private static final Logger log = Logger.getLogger(BookingControlServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Booking booking = ServiceFactory.getBookingService().getBookingById(Long.parseLong(req.getParameter("bookingId")));
            req.setAttribute("booking", booking);
            try {
                req.setAttribute("user", ServiceFactory.getUserService().getUserById(booking.getUserId()));
            } catch (UserServiceException e) {
                log.error(e);
                req.setAttribute("errorMessage", e.getMessage());
                req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req, resp);
            }
            List<BookingItem> bookingItems = ServiceFactory.getBookingService().getBookingItemsByBookingId(booking.getId());
            for (BookingItem item : bookingItems) {
                HotelRoom bookedRoom = ServiceFactory.getHotelRoomService().getRoomById(item.getRoom().getId());
                item.setRoom(bookedRoom);
            }
            req.setAttribute("bookingItems", bookingItems);
            req.getRequestDispatcher("/jsp-pages/bookingControl.jsp").forward(req, resp);
        } catch (HotelRoomServiceException | BookingServiceException e){
            log.error(e);
            req.setAttribute("errorMessage","There are problems with the contents of a booking ("+ e.getMessage()+")");
        }
    }
}
