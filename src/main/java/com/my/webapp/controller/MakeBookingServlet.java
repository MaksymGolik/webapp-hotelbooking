package com.my.webapp.controller;

import com.my.db.entity.*;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/makeBooking")
public class MakeBookingServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MakeBookingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp-pages/makeBooking.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Booking booking = new Booking();
        booking.setGuestSurname(req.getParameter("surname"));
        booking.setGuestName(req.getParameter("name"));
        booking.setGuestPhoneNumber(req.getParameter("phoneNumber"));

        try {
            for (BookingStatus status : ServiceFactory.getBookingService().getStatuses()) {
                if (status.getName().equals(BookingStatus.StatusName.CREATED)) {
                    booking.setStatus(status);
                    break;
                }
            }
        } catch (BookingServiceException e){
            log.error(e);
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }

        long userId = ((User)req.getSession().getAttribute("currentUser")).getId();
        booking.setUserId(userId);
        String[] rooms = ((String)req.getSession().getAttribute("rooms")).split(" ");
        req.getSession().removeAttribute("rooms");
        String[] period = ((String)req.getSession().getAttribute("period")).split(":");
        req.getSession().removeAttribute("period");

        for(String room : rooms){
            try {
                if(!ServiceFactory.getHotelRoomService().checkRoomAvailabilityByDates(Long.parseLong(room),Date.valueOf(period[0]),Date.valueOf(period[1]))){
                    req.setAttribute("errorMessage","Здається одна чи декілька з обраних вами кімнат вже заброньвані, пошукайте інший варінт");
                    req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
                }
            } catch (HotelRoomServiceException e) {
                log.error(e);
                req.setAttribute("errorMessage",e.getMessage());
                req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
            }
        }

        try {
            long bookingId = ServiceFactory.getBookingService().insertBooking(booking);
            List<BookingItem> items = new ArrayList<>();

            for (String roomId : rooms) {
                BookingItem item = new BookingItem();
                item.setBookingId(bookingId);
                HotelRoom room = new HotelRoom();
                room.setId(Long.parseLong(roomId));
                item.setRoom(room);
                item.setCheckInDate(Date.valueOf(period[0]));
                item.setCheckOutDate(Date.valueOf(period[1]));
                items.add(item);
            }
            ServiceFactory.getBookingService().insertBookingItems(items);
            req.setAttribute("bookingId",bookingId);
            req.getRequestDispatcher("/jsp-pages/bookingConfirmPage.jsp").forward(req,resp);
        } catch (BookingServiceException e){
            log.error(e);
            req.setAttribute("errorMessage",e.getMessage());
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }
    }
}
