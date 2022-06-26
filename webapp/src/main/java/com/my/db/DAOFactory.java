package com.my.db;

public class DAOFactory {
    private static IDAO dao = null;

    public static synchronized IDAO getDAOInstance(TypeDAO type){
        if(type == TypeDAO.MySQL){
            if(dao == null){
                dao = new MySQLDAO();
            }
            else return dao;
        }
        return dao;
    }
}
