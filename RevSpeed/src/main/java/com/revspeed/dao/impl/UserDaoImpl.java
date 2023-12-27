package com.revspeed.dao.impl;

import com.revspeed.dao.ServicesDao;
import com.revspeed.dao.UserDao;
import com.revspeed.db.DB;
import com.revspeed.model.User;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revspeed.services.ServicesService;
import com.revspeed.utility.GEmailSender;
import com.revspeed.utility.OptOutPlan;
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
        System.out.println("\n ***************************************");
        System.out.println("*     Welcome to RevSpeed Registration    *");
        System.out.println(" ***************************************\n");
        System.out.println("+-------------------------+");
        System.out.println("|   User Data Entry Form   |");
        System.out.println("+-------------------------+");

        System.out.print("| Enter your name:        ");
        String name = sc.nextLine();
        System.out.println("+-------------------------+");

        Long phone_number = 0L;
        boolean exception;
        do {
            System.out.print("| Enter your phone number: ");
            try {
                exception = false;
                phone_number = sc.nextLong();
            }catch (InputMismatchException e){
                System.out.println("Please enter a valid phone number !");
                exception = true;
                sc.nextLine();  // Clear the buffer
            }
        }while (exception);

        sc.nextLine(); // consume the newline character
        System.out.println("+-------------------------+");

        System.out.print("| Enter your address:      ");
        String address = sc.nextLine();
        System.out.println("+-------------------------+");

        String email_id;
        boolean emailExist, emailValid;
        do {
            System.out.print("| Enter your email:        ");
            email_id = sc.nextLine();

            emailExist = isEmailExist(email_id);
            emailValid = isEmailValid(email_id);

            if (emailExist) {
                System.out.println("| Please enter a different email ! |");
                System.out.println("+-------------------------+");
            }
            if (!emailValid) {
                System.out.println("| Entered email is not valid. Please enter a valid email ! |");
                System.out.println("+-------------------------+");
            }
        } while (emailExist || !emailValid);

        System.out.print("| Enter your password:     ");
        String password = sc.nextLine();
        System.out.println("+-------------------------+");

        user = new User(name, phone_number, address, email_id, password);


        try (CallableStatement callableStatement = connection.prepareCall("{CALL insertUser(?, ?, ?, ?, ?)}")) {
            callableStatement.setString(1, user.getName());
            callableStatement.setLong(2, user.getPhoneNumber());
            callableStatement.setString(3, user.getAddress());
            callableStatement.setString(4, user.getEmailId());
            callableStatement.setString(5, user.getPassword());
            callableStatement.executeUpdate();
            System.out.println("+-------------------------+");
            System.out.println("|  Registration Successful |");
            System.out.println("+-------------------------+");
            GEmailSender gEmailSender = new GEmailSender();

            gEmailSender.sendRegistrationEmail(user.getEmailId());
        }
    }

    static int id;
    static String email;
    @Override
    public void loginUser() throws SQLException {

        String LOGIN_PROCEDURE = "{CALL loginUser(?, ?)}";
        boolean loginSuccessful = false;

        User user = new User();

        do {
            System.out.println("\n ***************************************");
            System.out.println("*        Welcome to RevSpeed Login       *");
            System.out.println(" ***************************************\n");
            System.out.print("* Enter your email:    ");
            String enteredEmail = sc.nextLine();

            System.out.print("* Enter your password: ");
            String enteredPassword = sc.nextLine();

            try (CallableStatement callableStatement = connection.prepareCall(LOGIN_PROCEDURE)) {
                callableStatement.setString(1, enteredEmail);
                callableStatement.setString(2, enteredPassword);

                    ResultSet resultSet = callableStatement.executeQuery();
                    if (resultSet.next() && resultSet.getInt("success") == 1) {

                        id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        Long phoneNumber = resultSet.getLong("phone_number");
                        String address = resultSet.getString("address");
                        email = resultSet.getString("email_id");
                        String password = resultSet.getString("password");
                        String role = resultSet.getString("role");
                        loginSuccessful = true;

                        System.out.println("\n ***************************************");
                        System.out.println("*          Login Successful!             *");
                        System.out.println(" ***************************************\n");

                        if (role != null) {
                            userProfile(id, name, phoneNumber, address, email, password, role);
                        } else {
                            System.out.println("Role is null. Unable to create Profile.");
                        }

                        // Get today's date
                        LocalDate today = LocalDate.now();

                        // Define a formatter for MySQL DATE format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        // Format the date
                        String formattedDate = today.format(formatter);

                        OptOutPlan.optOutPlan(formattedDate);

                    } else {
                        System.out.println(" \n***************************************");
                        System.out.println("*   Invalid Credentials. Try Again.     *");
                        System.out.println(" ***************************************");
                    }
                }
        } while (!loginSuccessful);
    }

    @Override
    public void userProfile(int id, String name, Long phoneNumber, String address, String email, String password, String role) throws SQLException {
        UserOperations userOperations = new UserOperations(connection);
        ServicesDao servicesDao = new ServicesDaoImpl();
        ServicesService servicesService = new ServicesService(servicesDao);
        if (role.equals("user")){
            int choise = 0;
            do {
                System.out.println("+-----------------------------------------------+");
                System.out.println("|          Welcome to RevSpeed Dashboard        |");
                System.out.println("+-----------------------------------------------+");
                System.out.println("| Press 1: See Profile                          |");
                System.out.println("| Press 2: Update Profile                       |");
                System.out.println("| Press 3: Reset Password                       |");
                System.out.println("| Press 4: View Services of RevSpeed            |");
                System.out.println("| Press 5: Show Bills                           |");
                System.out.println("| Press 6: Show My OTT Platforms                |");
                System.out.println("| Press 7: Delete Profile                       |");
                System.out.println("+-----------------------------------------------+");


                System.out.print("* Enter your choice: ");
                int check = sc.nextInt();
                System.out.println();
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
                        servicesService.seeServices();
                        break;
                    case 5:
                        servicesService.seeMyBill(id);
                        break;
                    case 6:
                        servicesService.getUsersOTTPlatforms(id);
                        break;
                    case 7:
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
