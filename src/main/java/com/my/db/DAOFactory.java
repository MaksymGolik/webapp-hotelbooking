package com.my.db;

import com.my.exception.DBConnectionException;

public abstract class DAOFactory {
    public abstract void open() throws DBConnectionException;
    public abstract void close();
    public abstract void beginTransaction() throws DBConnectionException;
    public abstract void commitTransaction() throws DBConnectionException;
    public abstract void rollbackTransaction() throws DBConnectionException;
    public abstract IUserDAO getUserDao();
    public abstract IHotelRoomDAO getHotelRoomDAO();
    public abstract IBookingDAO getBookingDAO();

    public static DAOFactory getDAOInstance(TypeDAO typeDAO){
        switch (typeDAO){
            case MySQL: return new MySQLDAOFactory();
            default: throw new IllegalStateException("Database not supported");
        }
    }
}
