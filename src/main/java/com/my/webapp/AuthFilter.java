package com.my.webapp;

import com.my.db.entity.Role;
import com.my.db.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> LOGGED_PATHS =
            Arrays.asList(Path.CABINET_PATH,Path.CHANGE_BOOKING_STATUS_PATH);
    private static final List<String> GUESTS_PATHS =
            Arrays.asList(Path.MAKE_BOOKING_PATH, Path.BOOKING_DETAILS_PATH,Path.BOOKING_INFO_BRIDGE_PATH,Path.CABINET_GUEST_BOOKING_PATH);
    private static final List<String> ADMINS_PATHS =
            Arrays.asList(Path.CABINET_ADMIN_PANEL_PATH);

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain)

            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;

        final HttpSession session = req.getSession();

        User currentUser = (User)session.getAttribute("currentUser");

        String errorMessage="";

        if(LOGGED_PATHS.contains(req.getServletPath())||GUESTS_PATHS.contains(req.getServletPath())||ADMINS_PATHS.contains(req.getServletPath())) {
            if (currentUser==null) {
                errorMessage = "You must be a registered user to access this page";
            } else {
                if(GUESTS_PATHS.contains(req.getServletPath())){
                    if(!currentUser.getRole().getName().equals(Role.RoleName.GUEST)){
                        errorMessage = "To access this page you must be a registered user with the role of a hotel guest";
                    }
                }
                if(ADMINS_PATHS.contains(req.getServletPath())){
                    if(!currentUser.getRole().getName().equals(Role.RoleName.ADMIN)){
                        errorMessage = "To access this page you must be a registered user with the role of a hotel admin";
                    }
                }
            }
        }

        if(!errorMessage.isEmpty()){
            req.setAttribute("errorMessage",errorMessage);
            req.getRequestDispatcher("/jsp-pages/error.jsp").forward(request,response);
        }

        filterChain.doFilter(request,response);
    }
}
