package com.my.webapp.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logout.
 * Delete session.
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        final HttpSession session = req.getSession();

        session.removeAttribute("password");
        session.removeAttribute("email");
        session.removeAttribute("role");

        resp.sendRedirect(req.getContextPath() + "/");
    }

}