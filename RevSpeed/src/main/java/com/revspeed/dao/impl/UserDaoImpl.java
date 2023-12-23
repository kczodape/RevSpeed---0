package com.revspeed.dao.impl;

import com.revspeed.application.Main;
import com.revspeed.dao.UserDao;
import com.revspeed.db.DB;
import com.revspeed.model.User;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDaoImpl implements UserDao {

     private Connection connection = DB.getConnection();


    public UserDaoImpl(Connection connection) throws SQLException {
        this.connection = connection;
    }

    static Scanner sc = new Scanner(System.in);

    @Override
    public void registerUser(User user) throws SQLException{
        System.out.println("Please enter your details");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your phone number: ");
        Long phone_number = sc.nextLong();
        sc.nextLine();

        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        String email_id;
        boolean emailExist, emailValid;
        do {
            System.out.print("Enter your email: ");
            email_id = sc.nextLine();

            emailExist = isEmailExist(email_id);
            emailValid = isEmailValid(email_id);

            if (emailExist) {
                System.out.println("Email already exists. Please enter a different email !");
            }
            if (!emailValid) {
                System.out.println("Entered email is not valid. Please enter a valid email !");
            }
        }while (emailExist || !emailValid);


        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        user = new User(name, phone_number, address, email_id, password);


        try (CallableStatement callableStatement = connection.prepareCall("{CALL insertUser(?, ?, ?, ?, ?)}")) {
                callableStatement.setString(1, user.getName());
                callableStatement.setLong(2, user.getPhoneNumber());
                callableStatement.setString(3, user.getAddress());
                callableStatement.setString(4, user.getEmailId());
                callableStatement.setString(5, user.getPassword());
                callableStatement.executeUpdate();
        }
        System.out.println("Registration successfull. Please Login !");
        loginUser();
    }

    @Override
    public void loginUser() throws SQLException {
        String LOGIN_QUERY = "SELECT * FROM User WHERE email_id = ? AND password = ?";
        boolean loginSuccessful = false;

        do {
            System.out.print("Enter your email: ");
            String enteredEmail = sc.nextLine();

            System.out.print("Enter your password: ");
            String enteredPassword = sc.nextLine();

            try (PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_QUERY)) {
                preparedStatement.setString(1, enteredEmail);
                preparedStatement.setString(2, enteredPassword);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Long phone_number = resultSet.getLong("phone_number");
                        String email = resultSet.getString("email_id");
                        String role = resultSet.getString("role");

                        // Do something with the user information
                        System.out.println("Login successful!");
                        System.out.println("User ID: " + userId);
                        System.out.println("Name: " + name);
                        System.out.println("Phone number: " + phone_number);
                        System.out.println("Email: " + email);
                        System.out.println("Role: " + role);

                        loginSuccessful = true;
                    } else {
                        // No matching user found for the given email and password
                        System.out.println("Invalid email or password. Please try again.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Log the exception or handle it as appropriate
                System.out.println("An error occurred. Please try again.");
            }
        } while (!loginSuccessful);
    }


    public boolean isEmailExist(String email_id) throws SQLException{
            CallableStatement callableStatement = connection.prepareCall("{CALL GetUserCountByEmail(?, ?)}");
            callableStatement.setString(1, email_id);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.execute();

            int userCount = callableStatement.getInt(2);

            if (userCount > 0) {
                return true;
            }

            return false;
        }

        public boolean isEmailValid(String email_id){
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email_id);
            return matcher.matches();
        }
}
