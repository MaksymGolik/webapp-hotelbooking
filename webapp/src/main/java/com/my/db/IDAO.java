package com.my.db;

import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.DBConnectionException;

import java.sql.SQLException;
import java.util.List;

public interface IDAO {
    boolean insertUser(User user) throws SQLException;
    List<Role> getRoles () throws SQLException;
    boolean userIsExist(String email, String password) throws SQLException;
    Role.RoleName getRoleNameByEmailPassword (String email, String password) throws SQLException;
    User getUserInfoByEmail(String email) throws SQLException;
    void open() throws DBConnectionException;
    void close();
}
