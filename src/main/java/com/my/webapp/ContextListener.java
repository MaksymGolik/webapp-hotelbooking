package com.my.webapp;

import com.my.db.DAOFactory;
import com.my.db.TypeDAO;
import com.my.exception.DBConnectionException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(ContextListener.class);

    /*
    * Check connection availability
    */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DAOFactory.getDAOInstance(TypeDAO.MySQL).open();
            log.info("Connection successful");
        } catch (DBConnectionException e) {
            log.fatal(e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DAOFactory.getDAOInstance(TypeDAO.MySQL).close();
    }
}
