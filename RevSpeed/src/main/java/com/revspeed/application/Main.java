package com.revspeed.application;


import com.revspeed.dao.UserDao;
import com.revspeed.dao.impl.UserDaoImpl;
import com.revspeed.db.DB;
import com.revspeed.model.User;
import com.revspeed.services.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {

        // Initialize database connection
        Connection connection = DB.getConnection();
        UserDao userDao = new UserDaoImpl(connection);
        UserService userService = new UserService(userDao);
        User user = new User();
        int choice=0;
        System.out.println("Welcome to RevSpeed");
        do {
            System.out.println("Press 1: For Login\nPress 2: For Registration\nPress 3: For Exit");

            int check = sc.nextInt();

            switch (check) {
                case 1:
                    userService.loginUser();
                    break;
                case 2:
                    userService.registerUser(user);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("please press valid key !");

            }

            System.out.println("Press 1 to continue or any key to exit !");
            choice=sc.nextInt();
        }while(choice==1);
    }
}