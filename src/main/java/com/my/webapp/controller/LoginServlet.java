package com.my.webapp.controller;

import com.my.db.entity.User;
import com.my.exception.UserServiceException;
import com.my.service.ServiceFactory;
import com.my.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp-pages/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            final String email = req.getParameter("email");
            final String password = req.getParameter("password");
            String encodedPassword = DigestUtils.sha256Hex(password);

            //Logged user.
            if (req.getSession().getAttribute("currentUser")!=null) {

                    resp.sendRedirect(req.getContextPath() + "/home");


            } else {
                try{
                    User currentUser = ServiceFactory.getUserService().getUserByEmail(email);
                    if (currentUser.getPassword().equals(encodedPassword)) {

                        req.getSession().setAttribute("currentUser", currentUser);

                        resp.sendRedirect(req.getContextPath() + "/home");
                    } else {
                        req.getSession().setAttribute("errorMessage", "Incorrect password");
                        resp.sendRedirect(req.getContextPath() + "/login");
                    }
                } catch (UserServiceException e){
                    log.error(e);
                    req.getSession().setAttribute("errorMessage", "Incorrect email");
                    resp.sendRedirect(req.getContextPath() + "/login");
                }
            }
    }
}
