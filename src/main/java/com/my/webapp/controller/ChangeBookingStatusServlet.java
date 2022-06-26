package com.my.webapp.controller;

import com.my.db.entity.BookingStatus;
import com.my.db.entity.User;
import com.my.exception.BookingServiceException;
import com.my.exception.UserServiceException;
import com.my.service.ServiceFactory;
import com.my.utils.MailSender;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/changeBookingStatus")
public class ChangeBookingStatusServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ChangeBookingStatusServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<BookingStatus> statusList = ServiceFactory.getBookingService().getStatuses();
            BookingStatus newStatus = new BookingStatus();
            for (BookingStatus status : statusList) {
                if (status.getName().equals(BookingStatus.StatusName.valueOf(req.getParameter("statusName")))) {
                    newStatus = status;
                    break;
                }
            }
            ServiceFactory.getBookingService().changeBookingStatus(Long.parseLong(req.getParameter("bookingId")), newStatus);
            long userId = ServiceFactory.getBookingService().getBookingById(Long.parseLong(req.getParameter("bookingId"))).getUserId();
            User user = ServiceFactory.getUserService().getUserById(userId);
            MailSender.getInstance().sendMessageAfterStatusChanging(user.getEmail(),getServletContext().getRealPath("/messages/statusChange.txt"),user.getName(),Long.parseLong(req.getParameter("bookingId")));
            resp.sendRedirect(req.getContextPath() + "/cabinet");
        } catch (BookingServiceException | UserServiceException e){
            log.error(e);
            req.setAttribute("errorMessage",e.getMessage());
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
        }
    }
}
