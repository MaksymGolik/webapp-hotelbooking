package com.my.webapp.controller;

import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.UserServiceException;
import com.my.service.ServiceFactory;
import com.my.utils.MailSender;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(RegisterServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp-pages/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String name = req.getParameter("name");
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");

        try{
            if(ServiceFactory.getUserService().getUserByEmail(email).getEmail()!= null){
                req.getSession().setAttribute("errorMessage", "This email is already registered");
                resp.sendRedirect(req.getContextPath() + "/login");
            }
        } catch (UserServiceException e){
            log.info(e);

            User user = new User();
            user.setName(name);
            user.setEmail(email.toLowerCase());
            String encodedPassword = DigestUtils.sha256Hex(password);
            user.setPassword(encodedPassword);
            try {
                for(Role role : ServiceFactory.getUserService().getRoles()){
                    if (role.getName().equals(Role.RoleName.GUEST)) {
                        user.setRole(role);
                        break;
                    }
                }
            } catch (UserServiceException ex) {
                log.error(ex);
                req.getSession().setAttribute("errorMessage", "Failed to set a role during registration");
                req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
            }
            try {
                ServiceFactory.getUserService().insertUser(user);
                MailSender.getInstance().sendMessageAfterRegistration(user.getEmail(),getServletContext().getRealPath("/messages/registration.txt"),user.getName());
            } catch (UserServiceException ex){
                log.error(ex);
                req.setAttribute("errorMessage",ex.getMessage());
                req.getRequestDispatcher("/jsp-pages/error.jsp").forward(req,resp);
            }
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
