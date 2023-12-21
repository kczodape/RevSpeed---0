package com.revspeed.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DB {

    static Connection connection = null;

    public static Connection getConnection() throws SQLException {

        if (connection != null){
            return connection;
        }

        ResourceBundle resourceBundle = ResourceBundle.getBundle("sql");
        String db = resourceBundle.getString("db");
        String username = resourceBundle.getString("username");
        String password = resourceBundle.getString("password");

        return getConnection(db, username, password);
    }

    public static Connection getConnection(String db, String username, String password) throws SQLException{

//        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?user="+username+"&password="+password);
//            System.out.println("Connection established........");
//        }catch (SQLException e){
//            e.printStackTrace();
//        }

        return connection;

    }
}
