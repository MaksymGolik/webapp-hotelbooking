package com.my.service;

import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.UserServiceException;

import java.util.List;

public interface IUserService {
    boolean insertUser(User user) throws UserServiceException;
    List<Role> getRoles () throws UserServiceException;
    User getUserByEmail(String email) throws UserServiceException;
    User getUserById(long id) throws UserServiceException;
}
