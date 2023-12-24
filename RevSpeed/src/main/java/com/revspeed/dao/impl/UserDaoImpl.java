package com.revspeed.dao.impl;

import com.revspeed.dao.UserDao;
import com.revspeed.db.DB;
import com.revspeed.model.User;

import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revspeed.utility.UserOperations;

public class UserDaoImpl implements UserDao {

     private Connection connection = DB.getConnection();
    InputStream inputStream;

    Scanner sc = new Scanner(System.in);


    public UserDaoImpl(Connection connection) throws SQLException {
        this.connection = connection;
    }

    public UserDaoImpl(Connection connection, InputStream inputStream) throws SQLException {
        this.connection = connection;
        this.sc = new Scanner(inputStream);
    }


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
            System.out.println("Registration Successful !");
        }
    }

    @Override
    public void loginUser() throws SQLException {

        String LOGIN_PROCEDURE = "{CALL loginUser(?, ?)}";
        boolean loginSuccessful = false;

        do {
            System.out.print("Enter your email: ");
            String enteredEmail = sc.nextLine();

            System.out.print("Enter your password: ");
            String enteredPassword = sc.nextLine();

            try (CallableStatement callableStatement = connection.prepareCall(LOGIN_PROCEDURE)) {
                callableStatement.setString(1, enteredEmail);
                callableStatement.setString(2, enteredPassword);

                    ResultSet resultSet = callableStatement.executeQuery();
                    if (resultSet.next() && resultSet.getInt("success") == 1) {
                        // Login successful
                        System.out.println("Login successful!");

                        // Retrieve user details from the ResultSet
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Long phoneNumber = resultSet.getLong("phone_number");
                        String address = resultSet.getString("address");
                        String email = resultSet.getString("email_id");
                        String password = resultSet.getString("password");
                        String role = resultSet.getString("role");
                        loginSuccessful = true;

                        if (role != null) {
                            userProfile(id, name, phoneNumber, address, email, password, role);
                        } else {
                            System.out.println("Role is null. Unable to create Profile.");
                        }

                    } else {
                        // No matching user found for the given email and password
                        System.out.print("Invalid email or password. Please try again.");
                    }
                }
        } while (!loginSuccessful);
    }

    @Override
    public void userProfile(int id, String name, Long phoneNumber, String address, String email, String password, String role) throws SQLException {
        UserOperations userOperations = new UserOperations(connection);
        if (role.equals("user")){
            int choise = 0;
            do {
                System.out.println("Press 1: See Profile\nPress 2: Update Profile\nPress 3: Reset Password\nPress 4: View Services of RevSpeed\nPress 5: See Broadband service plan\nPress 6: See DTH service plan\nPress 7: Show bills\nPress 8: Delete profile");
                int check = sc.nextInt();

                switch (check) {
                    case 1:
                        UserOperations.seeProfile(name, phoneNumber, address, email, password, role);
                        break;
                    case 2:
                        userOperations.updateProfile(id);
                        break;
                    case 3:
                        userOperations.resetPassword(id, password);
                        break;
                    case 4:
                        System.out.println("See Services of RevSpeed");
                        break;
                    case 5:
                        System.out.println("View Broadband service plan is in progress");
                        break;
                    case 6:
                        System.out.println("See DTH service plan is in progress");
                        break;
                    case 7:
                        System.out.println("Show bills is in progress");
                        break;
                    case 8:
                        userOperations.deleteUser(id);
                        break;
                    default:
                        System.out.println("please press valid key !");
                }
                System.out.println("Want to contineu Press: 1 Or Any key");
                choise = sc.nextInt();
            }while (choise==1);
            System.setIn(System.in);
        }
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
