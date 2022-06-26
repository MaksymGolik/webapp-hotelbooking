package com.my.db;

import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.DataNotFoundException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDAOImpl implements IUserDAO{

    private static final Logger log = Logger.getLogger(MySQLUserDAOImpl.class);

    private Connection connection;

    public MySQLUserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    private static final String GET_ALL_ROLES = "SELECT * FROM user_role";
    private static final String INSERT_USER = "INSERT INTO user(role_id,email,password,name) VALUES (?,?,?,?)";
    private static final String GET_USER_BY_EMAIL = "SELECT user.id, user_role.id, user_role.name, user.email, user.name, user.password FROM user JOIN user_role ON user.role_id=user_role.id WHERE email = ?";
    private static final String GET_USER_BY_ID = "SELECT user.id, user_role.id, user_role.name, user.email, user.name, user.password FROM user JOIN user_role ON user.role_id=user_role.id WHERE user.id = ?";

    private User extractUser (ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(1));
        Role role = new Role();
        role.setId(resultSet.getLong(2));
        role.setName(Role.RoleName.valueOf(resultSet.getString(3).toUpperCase()));
        user.setRole(role);
        user.setEmail(resultSet.getString(4));
        user.setName(resultSet.getString(5));
        user.setPassword(resultSet.getString(6));
        return user;
    }
    public boolean insertUser(User user) {
        int affectedRows = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setLong(1, user.getRole().getId());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getName());
            affectedRows = preparedStatement.executeUpdate();
            MySQLDAOFactory.close(preparedStatement);
        }
        catch (SQLException ex){
            log.error(ex);
        }
        return affectedRows == 1;
    }

    public List<Role> getRoles () throws DataNotFoundException {
        List<Role> roleList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_ROLES);
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong(1));
                role.setName(Role.RoleName.valueOf(resultSet.getString(2).toUpperCase()));
                roleList.add(role);
            }
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(statement);
            if(roleList.size()==0) throw new DataNotFoundException();
        }
        catch (SQLException ex){
            log.error(ex);
            throw new DataNotFoundException();
        }
        return roleList;
    }

    public User getUserByEmail(String email) throws DataNotFoundException {
        User user;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = extractUser(resultSet);
            } else {
                throw new DataNotFoundException();
            }

        } catch (SQLException ex) {
            log.error(ex);
            throw new DataNotFoundException();
        } finally {
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
        }
        return user;
    }

    public User getUserById(long id) throws DataNotFoundException {
        User user = new User();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(GET_USER_BY_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = extractUser(resultSet);
            } else {
                throw new DataNotFoundException();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataNotFoundException();
        } finally {
            MySQLDAOFactory.close(resultSet);
            MySQLDAOFactory.close(preparedStatement);
        }
        return user;
    }
}
