package com.revspeed.application;

import com.revspeed.dao.UserDao;
import com.revspeed.dao.impl.UserDaoImpl;
import com.revspeed.db.DB;
import com.revspeed.model.User;
import com.revspeed.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    private static final Logger logger  = LoggerFactory.getLogger(Main.class);

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        // Initialize database connection
        Connection connection = DB.getConnection();
        UserDao userDao = new UserDaoImpl(connection);
        UserService userService = new UserService(userDao);
        User user = new User();
        int choice = 0;

        System.out.println("Welcome to RevSpeed");
        logger.info("User entered to the application");
        boolean exceptionOcuered;
        do {
            try {
                exceptionOcuered = false;  // Reset to false before each attempt
                do {
                    System.out.println("**************************************\n");
                    System.out.println("        Welcome to RevSpeed         \n");
                    System.out.println("**************************************");
                    System.out.println("\n*    Press 1: For Login              *");
                    System.out.println("*    Press 2: For Registration       *");
                    System.out.println("*    Press 3: For Exit               *\n");

                    System.out.print("\n*    Please Enter a key: ");
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
                            System.out.println("Please press a valid key !");
                            logger.warn("User press wrong key");
                    }

                    System.out.println("Press 1 to continue or any key to exit !");
                    choice = sc.nextInt();
                } while (choice == 1);
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid key !");
                logger.error("User entered wrong value in the starting of application.");
                exceptionOcuered = true;
                sc.nextLine();  // Clear the buffer
            }
        } while (exceptionOcuered);
    }
}
