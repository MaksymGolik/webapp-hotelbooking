package com.my.webapp.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cabinet")
public class CabinetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String email = (String) req.getSession().getAttribute("email");
        System.out.println("CabinetServlet#doGet");
        try {
            req.setAttribute("userInfo", DBUtils.dao.getUserInfoByEmail(email));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/jsp-pages/cabinet.jsp").forward(req,resp);
    }
}
