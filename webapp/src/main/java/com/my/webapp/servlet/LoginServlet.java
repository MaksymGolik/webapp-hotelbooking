package com.my.webapp.controller;

import com.my.db.entity.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet#doGet");
        req.getRequestDispatcher("/WEB-INF/jsp-pages/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String email = req.getParameter("email");
        System.out.println("LoginServlet#doPost");
            final String password = req.getParameter("password");


            final HttpSession session = req.getSession();

            //Logged user.
            if (nonNull(session) &&
                    nonNull(session.getAttribute("email")) &&
                    nonNull(session.getAttribute("password"))) {

                final Role.RoleName roleName = (Role.RoleName) session.getAttribute("role");

                moveToMenu(req, resp);


            } else {
                try {
                    if (DBUtils.dao.userIsExist(email, password)) {

                        final Role.RoleName roleName = DBUtils.dao.getRoleNameByEmailPassword(email, password);

                        req.getSession().setAttribute("password", password);
                        req.getSession().setAttribute("email", email);
                        req.getSession().setAttribute("role", roleName);


                        moveToMenu(req, resp);

                    } else {

                        req.getRequestDispatcher("/WEB-INF/jsp-pages/login.jsp").forward(req, resp);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * Move user to menu.
     * If access 'admin' move to admin menu.
     * If access 'user' move to user menu.
     */
    private void moveToMenu(final HttpServletRequest req,
                            final HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp-pages/main.jsp").forward(req,res);
    }
}
