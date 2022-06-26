package com.my.service;

import com.my.db.*;
import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.DBConnectionException;
import com.my.exception.DataNotFoundException;
import com.my.exception.UserServiceException;
import com.my.utils.MailSender;
import com.my.utils.Validator;
import org.apache.log4j.Logger;

import java.util.List;

public class UserService implements IUserService{

    private static final Logger log = Logger.getLogger(UserService.class);

    private static final TypeDAO type = TypeDAO.MySQL;
    private static DAOFactory dao;
    private static IUserDAO userDAO;

    static {
        dao = DAOFactory.getDAOInstance(type);
        userDAO = dao.getUserDao();
    }

    @Override
    public synchronized boolean insertUser(User user) throws UserServiceException {
        boolean result;
        try {
            dao.open();
            userDAO = dao.getUserDao();
            if(Validator.validateUser(user)){
                result = userDAO.insertUser(user);
                } else throw new UserServiceException("Uncorrected user data");
            if(!result) throw new UserServiceException("Cannot create a new user");
        } catch (DBConnectionException e) {
            log.error(e);
            return false;
        } finally {
            dao.close();
        }
        return result;
    }

    @Override
    public List<Role> getRoles() throws UserServiceException {
        List<Role> roleList;
        try{
            dao.open();
            userDAO = dao.getUserDao();
            roleList = userDAO.getRoles();
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new UserServiceException("Cannot determine role");
        }
        return roleList;
    }

    @Override
    public User getUserByEmail(String email) throws UserServiceException {
        User user;
        try{
            dao.open();
            userDAO = dao.getUserDao();
            user = userDAO.getUserByEmail(email);
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new UserServiceException();
        }
        return user;
    }

    @Override
    public User getUserById(long id) throws UserServiceException {
        User user;
        try{
            dao.open();
            userDAO = dao.getUserDao();
            user = userDAO.getUserById(id);
            dao.close();
        }catch (DBConnectionException | DataNotFoundException e){
            log.error(e);
            throw new UserServiceException();
        }
        return user;
    }
}
