package com.my.webapp;

import com.my.db.entity.Role;
import com.my.db.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final List<String> PROTECTED_PATHS = Arrays.asList();

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain)

            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        if(PROTECTED_PATHS.contains(req.getServletPath())) {
            final HttpSession session = req.getSession();

            //Logged user.
            if (nonNull(session) &&
                    nonNull(session.getAttribute("email")) &&
                    nonNull(session.getAttribute("password"))) {

                final Role.RoleName roleName = (Role.RoleName) session.getAttribute("role");

            } else {
                request.getRequestDispatcher("/WEB-INF/jsp-pages/error.jsp").forward(request,response);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
