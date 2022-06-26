package com.my.webapp;

import com.my.db.DAOFactory;
import com.my.db.TypeDAO;
import com.my.exception.DBConnectionException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ContextListener#contextInitialized");
        try {
            DAOFactory.getDAOInstance(TypeDAO.MySQL).open();
        } catch (DBConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ContextListener#contextDestroyed");
        DAOFactory.getDAOInstance(TypeDAO.MySQL).close();
    }
}
