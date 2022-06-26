package com.my.webapp.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bookingInfoBridge")
public class BookingInfoBridgeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("rooms",req.getParameter("rooms"));
        req.getSession().setAttribute("period",req.getParameter("period"));
        resp.sendRedirect(req.getContextPath() + "/makeBooking");
    }
}
