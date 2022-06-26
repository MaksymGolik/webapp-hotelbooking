package com.my.db;

import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.DataNotFoundException;

import java.util.List;

public interface IUserDAO {
    boolean insertUser(User user);
    List<Role> getRoles () throws DataNotFoundException;
    User getUserByEmail(String email) throws DataNotFoundException;
    User getUserById(long id) throws DataNotFoundException;
}
