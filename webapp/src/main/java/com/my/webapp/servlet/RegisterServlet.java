package com.my.webapp.controller;



import com.my.db.entity.Role;
import com.my.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp-pages/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final String name = req.getParameter("name");
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");

        User user = new User();
        user.setName(name);
        user.setEmail(email.toLowerCase());
        user.setPassword(password);
        try {
            List<Role> roleList = DBUtils.dao.getRoles();
            roleList.forEach(role -> {
                if(role.getName().equals(Role.RoleName.GUEST)){
                    user.setRole(role);
                }
            });
            DBUtils.dao.insertUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (DBUtils.dao.userIsExist(email, password)) {

                final Role.RoleName roleName = DBUtils.dao.getRoleNameByEmailPassword(email, password);

                req.getSession().setAttribute("password", password);
                req.getSession().setAttribute("email", email);
                req.getSession().setAttribute("role", roleName);
            }
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
