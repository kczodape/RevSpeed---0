package com.revspeed.application;


import com.revspeed.dao.UserDao;
import com.revspeed.dao.impl.UserDaoImpl;
import com.revspeed.db.DB;
import com.revspeed.model.User;
import com.revspeed.services.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {

        UserService userService = new UserService();
        User user = new User();

        System.out.println("Welcome to RevSpeed");
        System.out.println("Press 1: For Login\nPress 2: For Registration\nPress 3: For Exit");

        int check = sc.nextInt();

        switch (check){
            case 1:
                System.out.println("Login functionality in progrees");
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

    }
}