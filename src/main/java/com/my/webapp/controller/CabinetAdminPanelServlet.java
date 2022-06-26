package com.my.webapp.controller;

import com.my.db.entity.BookingStatus;
import com.my.exception.BookingServiceException;
import com.my.service.ServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/adminPanel")
public class CabinetAdminPanelServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CabinetAdminPanelServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BookingStatus> statuses = new ArrayList<>();
        try {
            statuses = ServiceFactory.getBookingService().getStatuses();
        } catch (BookingServiceException e){
            log.error(e);
            req.setAttribute("errorMessage",e.getMessage());
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }
        BookingStatus requestedStatus = new BookingStatus();
        for(BookingStatus status : statuses){
            if(status.getName().equals(BookingStatus.StatusName.valueOf(req.getParameter("bookingStatus")))){
                requestedStatus = status;
                break;
            }
        }
        try {
            req.setAttribute("bookingList", ServiceFactory.getBookingService().getBookingsByStatus(requestedStatus));
        } catch (BookingServiceException e){
            log.info(e);
            req.setAttribute("errorMessage",e.getMessage());
        } finally {
            req.getRequestDispatcher("/jsp-pages/cabinetAdminPanel.jsp").forward(req,resp);
        }
    }
}
