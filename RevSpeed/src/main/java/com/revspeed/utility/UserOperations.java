package com.revspeed.utility;

import com.revspeed.db.DB;

import java.sql.*;
import java.util.Scanner;

//import static com.revspeed.utility.Profile.sc;

public class UserOperations {
    Scanner sc = new Scanner(System.in);
    public Connection connection;

    {
        try {
            connection = DB.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserOperations(Connection connection){
        this.connection = connection;
    }

    public static void seeProfile(String name, Long phoneNumber, String address, String email, String password, String role){
        System.out.println("+-------------------------------------------+");
        System.out.printf("| %-20s %-20s |\n", "Name:", name);
        System.out.println("+-------------------------------------------+");
        System.out.printf("| %-20s %-20s |\n", "Phone number:", phoneNumber);
        System.out.println("+-------------------------------------------+");
        System.out.printf("| %-20s %-20s |\n", "Address:", address);
        System.out.println("+-------------------------------------------+");
        System.out.printf("| %-20s %-20s |\n", "Email:", email);
        System.out.println("+-------------------------------------------+");    }

    public void updateProfile(int userId) throws SQLException {

        System.out.println("+--------------------------------------------------+");
        System.out.println("|          User Data Update Menu                    |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("| Enter the field No. which you want to update:    |");
        System.out.println("|                                                  |");
        System.out.println("| 1. Name                                          |");
        System.out.println("| 2. Phone Number                                  |");
        System.out.println("| 3. Address                                       |");
        System.out.println("| 4. Email                                         |");
        System.out.println("| 5. Password                                      |");
        System.out.println("| 6. Update All                                    |");
        System.out.println("+--------------------------------------------------+");

        System.out.print("* Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();  // Consume the newline character

        String name = null;
        Long phoneNumber = null;
        String address = null;
        String email = null;
        String password = null;

        System.out.println("+-------------------------------------------------+");
        switch (choice) {
            case 1:
                System.out.println("| Option 1: Update Name                           |");
                System.out.print("| Enter new name: ");
                name = sc.nextLine();
                break;
            case 2:
                System.out.println("| Option 2: Update Phone Number                   |");
                System.out.print("| Enter new phone number: ");
                if (sc.hasNextLong()) {
                    phoneNumber = sc.nextLong();
                    sc.nextLine(); // Consume the newline character
                } else {
                    System.out.println("| Invalid input. Phone number not updated.       |");
                    System.out.println("+-------------------------------------------------+");
                    return;
                }
                break;
            case 3:
                System.out.println("| Option 3: Update Address                        |");
                System.out.print("| Enter new address: ");
                address = sc.nextLine();
                break;
            case 4:
                System.out.println("| Option 4: Update Email                          |");
                System.out.print("| Enter new email: ");
                email = sc.nextLine();
                break;
            case 5:
                System.out.println("| Option 5: Update Password                       |");
                System.out.print("| Enter new password: ");
                password = sc.nextLine();
                break;
            case 6:
                System.out.println("| Option 6: Update All                            |");
                System.out.print("| Enter new name: ");
                name = sc.nextLine();

                System.out.print("| Enter new phone number: ");
                if (sc.hasNextLong()) {
                    phoneNumber = sc.nextLong();
                    sc.nextLine(); // Consume the newline character
                } else {
                    System.out.println("| Invalid input. Phone number not updated.       |");
                    System.out.println("+-------------------------------------------------+");
                    return;
                }

                System.out.print("| Enter new address: ");
                address = sc.nextLine();

                System.out.print("| Enter new email: ");
                email = sc.nextLine();

                System.out.print("| Enter new password: ");
                password = sc.nextLine();
                break;
            default:
                System.out.println("| Invalid choice                                 |");
                System.out.println("+-------------------------------------------------+");
                return;
        }

        try (CallableStatement callableStatement = connection.prepareCall("{CALL updateUserProfile(?, ?, ?, ?, ?, ?)}")) {
            callableStatement.setInt(1, userId);
            callableStatement.setString(2, name);

            // Check if phoneNumber is null before invoking longValue()
            if (phoneNumber != null) {
                callableStatement.setLong(3, phoneNumber.longValue());
            } else {
                callableStatement.setNull(3, Types.BIGINT); // Set to NULL in the database
            }

            callableStatement.setString(4, address);
            callableStatement.setString(5, email);
            callableStatement.setString(6, password);

            callableStatement.executeUpdate();

            System.out.println("+------------------------------------+");
            System.out.println("|   Profile updated successfully!   |");
            System.out.println("+------------------------------------+");        }
    }

    public void resetPassword(int userId, String password) throws SQLException {
        boolean match = false;
        do {
            System.out.print("+----------------------------------------+\n");
            System.out.printf("| Please enter Old Password: ");
            String oldPassword = sc.nextLine();
            System.out.printf("| Please enter new password: ");
            String newPassword = sc.nextLine();
            System.out.print("+----------------------------------------+\n");


            if (password.equalsIgnoreCase(oldPassword)){

                System.out.println("Password matches. Updating password...");

                String resetPasswordQueery = "UPDATE user SET password = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(resetPasswordQueery);
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();

                System.out.println("Password updated successfully!");
                match = false;
            } else {
                System.out.println("Password does not match. Password not updated!");
                match = true;
            }
        }while (match);
    }

    public void deleteUser(int userId) throws SQLException{
        System.out.println("Press 1: Confirm delete\nPress 0: Cancel");
        int userChoice = sc.nextInt();

        String deleteUserQuery = (userChoice == 1) ? "DELETE FROM user WHERE id = ?" : "SELECT 'Cancel'";

        PreparedStatement preparedStatement = connection.prepareStatement(deleteUserQuery);
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();

    }

}
