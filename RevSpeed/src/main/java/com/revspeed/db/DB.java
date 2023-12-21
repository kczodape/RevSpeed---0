package com.revspeed.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    static Connection connection = null;
    public static Connection getConnection() throws SQLException {

        if (connection != null){
            return connection;
        }

        String db = "RevSpeed";
        String username = "root";
        String password = "7872";

        return getConnection(db, username, password);
    }

    public static Connection getConnection(String db, String username, String password) throws SQLException{

//        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?user="+username+"&password="+password);
            System.out.println("Connection established........");
//        }catch (SQLException e){
//            e.printStackTrace();
//        }

        return connection;

    }
}
