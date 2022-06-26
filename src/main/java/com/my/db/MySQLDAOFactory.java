package com.my.db;

import com.my.exception.DBConnectionException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ResourceBundle;

public class MySQLDAOFactory extends DAOFactory{

    private static Connection connection;
    private static HikariDataSource ds;

    MySQLDAOFactory(){
        ResourceBundle dbConfig = ResourceBundle.getBundle("dbConfig");
        //configuring db connection and pooling with recommended params
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + dbConfig.getString("host") + ":" + dbConfig.getString("port") + "/" + dbConfig.getString("database"));
        config.setUsername(dbConfig.getString("user"));
        config.setPassword(dbConfig.getString("password"));
        config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        config.setAutoCommit(true);
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBConnectionException(e.getMessage());
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

    /** Transaction methods */
    public void beginTransaction() throws DBConnectionException {
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
    }

    public void commitTransaction() throws DBConnectionException {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
    }

    public void rollbackTransaction() throws DBConnectionException {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IUserDAO getUserDao() {
        return new MySQLUserDAOImpl(connection);
    }

    @Override
    public IHotelRoomDAO getHotelRoomDAO() {
        return new MySQLHotelRoomDAOImpl(connection);
    }

    @Override
    public IBookingDAO getBookingDAO() {
        return new MySQLBookingDAOImpl(connection);
    }
}
