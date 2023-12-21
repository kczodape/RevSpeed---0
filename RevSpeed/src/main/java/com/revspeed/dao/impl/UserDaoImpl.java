package com.revspeed.dao.impl;

import com.revspeed.dao.UserDao;
import com.revspeed.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UserDaoImpl implements UserDao {

    private Connection connection;
    static Scanner sc = new Scanner(System.in);

    public UserDaoImpl(Connection connection){
        this.connection = connection;
    }
    @Override
    public void registerUser(User user) {
        System.out.println("Please enter your details");
        System.out.println("\n");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter your phone number: ");
        Long phone_number = sc.nextLong();
        System.out.print("Enter your address: ");
        String address = sc.nextLine();
        System.out.print("Enter your email: ");
        String email_id = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        user = new User(name, phone_number, address, email_id, password);

        String insertQuery = "INSERT INTO User (name, phone_number, address, email_id, password) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setLong(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getAddress());
            preparedStatement.setString(4, user.getEmailId());
            preparedStatement.setString(5, user.getPassword());

            preparedStatement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
