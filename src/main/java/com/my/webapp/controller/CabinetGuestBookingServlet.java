package com.my.webapp.controller;

import com.my.db.entity.User;
import com.my.exception.BookingServiceException;
import com.my.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/myBookings")
public class CabinetGuestBookingServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CabinetGuestBookingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User)req.getSession().getAttribute("currentUser");
        try {
            req.setAttribute("bookingList", ServiceFactory.getBookingService().getBookingsByUserId(currentUser.getId()));
        } catch (BookingServiceException e){
            log.info(e);
            req.setAttribute("errorMessage",e.getMessage());
        } finally {
            req.getRequestDispatcher("/jsp-pages/cabinetGuestBookingSection.jsp").forward(req,resp);
        }
    }
}
