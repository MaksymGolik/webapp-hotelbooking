package com.my.db;

import com.my.db.entity.Role;
import com.my.db.entity.User;
import com.my.exception.DBConnectionException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MySQLDAO implements IDAO{

    private static Connection connection;
    private static HikariDataSource ds;

    private static final String GET_ALL_ROLES = "SELECT * FROM user_role";
    private static final String INSERT_USER = "INSERT INTO user(role_id,email,password,name) VALUES (?,?,?,?)";
    private static final String GET_ALL_USERS= "SELECT user.id, role_id, user_role.name, email, user.name FROM user JOIN user_role ON user.role_id=user_role.id";
    private static final String GET_USER_ID_BY_EMAIL_PASSWORD = "SELECT user.id FROM user WHERE email = ? AND password = ?";
    private static final String GET_ROLE_BY_EMAIL_PASSWORD = "SELECT user_role.name FROM user JOIN user_role ON user.role_id=user_role.id WHERE email = ? AND password = ?";
    private static final String GET_USER_INFO_BY_EMAIL_PASSWORD = "SELECT user_role.name, user.name FROM user JOIN user_role ON user.role_id=user_role.id WHERE email = ?";


    MySQLDAO(){
        ResourceBundle dbConfig = ResourceBundle.getBundle("dbConfig");
        //configuring db connection and pooling with recommended params
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + dbConfig.getString("host") + ":" + dbConfig.getString("port") + "/" + dbConfig.getString("database"));
        config.setUsername(dbConfig.getString("user"));
        config.setPassword(dbConfig.getString("password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        ds = new HikariDataSource(config);
    }

    private static Connection getConnection() throws DBConnectionException {
        try {
            return ds.getConnection();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new DBConnectionException();
        }
    }

    public void open() throws DBConnectionException {
        connection = getConnection();
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertUser(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
        preparedStatement.setInt(1,user.getRole().getId());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3,user.getPassword());
        preparedStatement.setString(4, user.getName());
        int affectedRows = preparedStatement.executeUpdate();
        DBUtils.close(preparedStatement);
        return affectedRows == 1;
    }

    private Role.RoleName defineRoleName (String roleNameString) {
        if (Role.RoleName.GUEST.toString().equals(roleNameString)) {
            return Role.RoleName.GUEST;
        } else {
            if (Role.RoleName.ADMIN.toString().equals(roleNameString)) {
                return Role.RoleName.ADMIN;
            } else {
                return Role.RoleName.UNKNOWN;
            }
        }
    }

    public List<Role> getRoles () throws SQLException {
        List<Role> roleList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(GET_ALL_ROLES);
        while(resultSet.next()){
            Role role = new Role();
            role.setId(resultSet.getInt(1));
            role.setName(defineRoleName(resultSet.getString(2)));
            roleList.add(role);
        }
        DBUtils.close(resultSet);
        DBUtils.close(statement);
        return roleList;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(GET_ALL_USERS);
        while(resultSet.next()){
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setEmail(resultSet.getString(4));
            user.setName(resultSet.getString(5));
            Role role = new Role();
            role.setId(resultSet.getInt(2));
            role.setName(defineRoleName(resultSet.getString(3)));
            user.setRole(role);
            userList.add(user);
        }
        DBUtils.close(resultSet);
        DBUtils.close(statement);
        return userList;
    }

    public boolean userIsExist(String email, String password) throws SQLException {
        int id = -1;
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ID_BY_EMAIL_PASSWORD);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            id = resultSet.getInt(1);
        }
        DBUtils.close(resultSet);
        DBUtils.close(preparedStatement);
        return id!=-1;
    }

    public Role.RoleName getRoleNameByEmailPassword (String email, String password) throws SQLException {
        Role.RoleName roleName = Role.RoleName.UNKNOWN;
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ROLE_BY_EMAIL_PASSWORD);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            roleName = defineRoleName(resultSet.getString(1));
        }
        DBUtils.close(resultSet);
        DBUtils.close(preparedStatement);
        return roleName;
    }

    public User getUserInfoByEmail(String email) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_INFO_BY_EMAIL_PASSWORD);
        preparedStatement.setString(1,email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            Role role = new Role();
            role.setName(defineRoleName(resultSet.getString(1)));
            user.setRole(role);
            user.setName(resultSet.getString(2));
        }
        DBUtils.close(resultSet);
        DBUtils.close(preparedStatement);
        return user;
    }
}
