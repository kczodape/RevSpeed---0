package com.revspeed.dao.impl;

import com.revspeed.dao.ServicesDao;
import com.revspeed.db.DB;
import com.revspeed.model.User;
import com.revspeed.utility.GEmailSender;
import com.revspeed.utility.OptOutPlan;

import java.lang.ref.PhantomReference;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ServicesDaoImpl implements ServicesDao {
    Connection connection = DB.getConnection();
    Scanner sc = new Scanner(System.in);
    int userId = UserDaoImpl.id;
    String userMail = UserDaoImpl.email;
    GEmailSender gEmailSender = new GEmailSender();
    public ServicesDaoImpl() throws SQLException {
    }

    @Override
    public void seeServices() throws SQLException {
        System.out.println("+------------------------------------------+");
        System.out.println("|          RevSpeed Offering Services      |");
        System.out.println("+------------------------------------------+");
        String seeServicesQuerry = "SELECT * FROM services";
        PreparedStatement preparedStatement = connection.prepareStatement(seeServicesQuerry);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String serviceName = resultSet.getString(2);

            System.out.printf("| Press %d: See %-27s |\n", id, serviceName);
        }
        System.out.println("+------------------------------------------+");
        System.out.print("* Enter your choice: ");

        int choice = sc.nextInt();

            switch (choice){
                case 1:
                    seeBroadbandService();
                    break;
                case 2:
                    seeDthService();
                    break;
                default:
                    System.out.println("Press valid key !");
            }
        System.out.println();
    }

    @Override
    public void seeBroadbandService() throws SQLException {
        String seeBroadband_serviceQuerry = "SELECT * FROM Broadband_service";
        PreparedStatement preparedStatement = connection.prepareStatement(seeBroadband_serviceQuerry);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("+---------------------------------+");
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String serviceName = resultSet.getString(3);
            System.out.printf("| Press %d: See %-10s plan    |\n", id, serviceName);
        }
        System.out.println("+---------------------------------+");
        System.out.print("* Enter your choice: ");
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                seeBroadbandServiceBasedOnPlan(choice, userId);
                break;
            case 2:
                seeBroadbandServiceBasedOnPlan(choice, userId);
                break;
            case 3:
                seeBroadbandServiceBasedOnPlan(choice, userId);
                break;
            default:
                System.out.println("please press valid key !");
            }
    }

    @Override
    public void seeBroadbandServiceBasedOnPlan(int id, int userId) throws SQLException {
        String seeBroadbandServiceBasedOnPlan = "SELECT * FROM Broadband_service_plans_details WHERE br_sr_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(seeBroadbandServiceBasedOnPlan);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("+----------------------------------------------------------------------------------------+");
        System.out.printf("| %-10s | %-60s | %-25s |\n", "Plan ID", "Plan Name", "Price");
        System.out.println("+----------------------------------------------------------------------------------------+");

        double price = 0.0;
        String broadbandPlanName = "";
        while (resultSet.next()){
            int brodbandServiceId = resultSet.getInt("br_sr_pl_dt_id");
            broadbandPlanName = resultSet.getString("plan_name");
            price = resultSet.getDouble("price");

            System.out.printf("| %-10s | %-60s | %-10s |\n", brodbandServiceId, broadbandPlanName, price);
        }
        System.out.println("+----------------------------------------------------------------------------------------+");

        CallableStatement callableStatement = connection.prepareCall("{CALL GetPlatformNameByBrSrId(?)}");
        callableStatement.setInt(1, id);
        ResultSet callableResultSet = callableStatement.executeQuery();
        System.out.println("+-------------------------------------------------------------------+");
        System.out.printf("| %-60s |\n", "You will get those OTT platform/platforms for chosen subscription");
        System.out.println("|                                                                   |");
        while (callableResultSet.next()){
            String platformName = callableResultSet.getString("platform_name");
            System.out.printf("| %-65s |\n", platformName);
        }
        System.out.println("+-------------------------------------------------------------------+");

        System.out.print("* Choose the plan ID to purchace: ");
        int selectedPlanId = sc.nextInt();

        Date purchaseDate = new Date(System.currentTimeMillis());
        int subscriptionDays = 0;

        switch (selectedPlanId) {
            case 1:
            case 2:
            case 3:
                subscriptionDays = 28;
                break;
            case 4:
            case 5:
            case 6:
                subscriptionDays = 84;
                break;
            case 7:
            case 8:
            case 9:
                subscriptionDays = 360;
                break;
            default:
                System.out.println("Invalid plan selected.");
        }

        // Calculate end date
        long endDateMillis = purchaseDate.getTime() + TimeUnit.DAYS.toMillis(subscriptionDays);
        Date endDate = new Date(endDateMillis);

        // Calculate two days before the endDate
        long twoDaysBeforeMillis = endDateMillis - TimeUnit.DAYS.toMillis(2);
        // Create a Date object for two days before the endDate
        Date twoDaysBeforeDate = new Date(twoDaysBeforeMillis);
        // Compare dates
        if (purchaseDate.compareTo(twoDaysBeforeDate) == 0) {
            gEmailSender.sendSubscriptionReminderEmail(userMail, broadbandPlanName, 2);
        }

        // Insert the purchased plan into User_Service_Link
        String insertUserSubscription = "INSERT INTO User_Service_Link (user_id, br_sr_dt_id, subscription_start_date, subscription_end_date, user_status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertSubscriptionStatement = connection.prepareStatement(insertUserSubscription);
        insertSubscriptionStatement.setInt(1, userId);
        insertSubscriptionStatement.setInt(2, selectedPlanId);
        insertSubscriptionStatement.setDate(3, purchaseDate);
        insertSubscriptionStatement.setDate(4, endDate);
        insertSubscriptionStatement.setInt(5, 1);

        // Execute the insert statement
        insertSubscriptionStatement.executeUpdate();
        System.out.println("+------------------------------------------+");
        System.out.println("|   Subscription purchased successfully!   |");
        System.out.println("+------------------------------------------+");

        gEmailSender.sendPurchaseConfirmationEmail(userMail, broadbandPlanName, price);
    }

    @Override
    public void getUsersOTTPlatforms(int id) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("{CALL GetOTTPlatformsForUser(?)}");
        callableStatement.setInt(1, id);
        ResultSet resultSet = callableStatement.executeQuery();

        System.out.println("+-------------------------+");
        System.out.printf("| %-22s |\n", "OTT Platforms");
        System.out.println("+-------------------------+");

        while (resultSet.next()) {
            String platformName = resultSet.getString("platform_name");
            System.out.printf("| %-22s |\n", platformName);
        }

        System.out.println("+-------------------------+");

    }

    @Override
    public void seeDthService() throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("{CALL GetLanguageOptions()}");
        ResultSet resultSet = callableStatement.executeQuery();
        System.out.println("+-------------------------------------------+");
        while (resultSet.next()){
            int languageId = resultSet.getInt(1);
            String language = resultSet.getString(2);
            System.out.printf("| Press %-2d : Choose %-5s language channels |\n", languageId, language);

        }
        System.out.println("+-------------------------------------------+");
        System.out.print("* Enter your choice: ");
        int check = sc.nextInt();
        switch (check){
            case 1:
                getPlansForLanguage("Hindi");
                break;
            case 2:
                getPlansForLanguage("Tamil");
                break;
            default:
                System.out.println("Please press valid key");
        }
    }

    @Override
    public void getPlansForLanguage(String language) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("{CALL GetPlansForLanguage(?)}");
        callableStatement.setString(1, language);
        ResultSet resultSet = callableStatement.executeQuery();
        System.out.println("+------------------------------------------+");
        System.out.printf("| %-10s | %-27s |\n", "Press", "Buy");
        System.out.println("+------------------------------------------+");
        String dthPlanName = "";
        while (resultSet.next()){
            int planId = resultSet.getInt(1);
            dthPlanName = resultSet.getString(2);
            System.out.printf("| Press %-3s | Buy %-24s |\n", planId, dthPlanName, "plan");
        }
        System.out.println("+------------------------------------------+");

        int check = sc.nextInt();
        switch (check){
            case 1:
                getPlansForCategory(check, dthPlanName);
                break;
            case 2:
                getPlansForCategory(check, dthPlanName);
                break;
            case 3:
                getPlansForCategory(check, dthPlanName);
                break;
            case 4:
                getPlansForCategory(check, dthPlanName);
                break;
            case 5:
                getPlansForCategory(check, dthPlanName);
                break;
            case 6:
                getPlansForCategory(check, dthPlanName);
                break;
            default:
                System.out.println("Please press valid key");
        }
    }

    @Override
    public void getPlansForCategory(int planId, String dthPlanName) throws SQLException {

        String insertUserLinkQuerry = "INSERT INTO User_Service_Link (user_id, dth_service_plan_id, subscription_start_date, subscription_end_date, user_status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertUserLinkStatement = connection.prepareStatement(insertUserLinkQuerry);

        // Assuming you have user_id
        insertUserLinkStatement.setInt(1, userId);  // Replace userId with the actual user_id

        // Get current date as start date
        LocalDate startDate = LocalDate.now();
        insertUserLinkStatement.setInt(2, planId);
        insertUserLinkStatement.setDate(3, java.sql.Date.valueOf(startDate));  // Convert LocalDate to java.sql.Date

        // Calculate end date as 28 days later
        LocalDate endDate = startDate.plusDays(28);
        insertUserLinkStatement.setDate(4, java.sql.Date.valueOf(endDate));  // Convert LocalDate to java.sql.Date

        insertUserLinkStatement.setInt(5, 1);

        // calculating date for sending  remind mail
        LocalDate remindingDate = endDate.minusDays(2);

        // Execute the insert statement
        int rowsAffected = insertUserLinkStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("DTH Plan purchased successfully");
            if (startDate.compareTo(remindingDate) == 0){
                gEmailSender.sendSubscriptionReminderEmail(userMail, dthPlanName, 2);
            }
        } else {
            System.out.println("Failed to purchase DTH Plan");
        }

        System.out.println("You got this plan");
        String getPlanForCategoryQuerry = "SELECT dth_chnl_dt_id, channel_name, price FROM DTH_channel_details WHERE dth_sr_pl_dt_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(getPlanForCategoryQuerry);
        preparedStatement.setInt(1, planId);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("+----------------------------------------+");
        System.out.printf("| %-29s | %-6s |\n", "Channel name", "Price");
        System.out.println("+----------------------------------------+");

        double totalDthChannelPrice = 0.0;
        while (resultSet.next()){
            int dthChnlId = resultSet.getInt(1);
            String channelName = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            System.out.printf("| %-20s | %-6.2f |\n", channelName, price);
            totalDthChannelPrice = totalDthChannelPrice + price;
        }
        System.out.println("+----------------------------------------+");
        gEmailSender.sendPurchaseConfirmationEmail(userMail, dthPlanName, totalDthChannelPrice);
    }

    @Override
    public void seeMyBill(int id) throws SQLException{
        System.out.println("Press 1: See BroadBand bill\nPress 2: See DTH bill");
        int check = sc.nextInt();
        switch (check){
            case 1:
                broadbandServiceBillDetails(id);
                break;
            case 2:
                dthServiceBillDetails(id);
                break;
            default:
                System.out.println("please press valid key");
        }
    }

    public void broadbandServiceBillDetails(int id) throws SQLException{
        CallableStatement callableStatement = connection.prepareCall("{CALL GetUserBroadbandDetails(?)}");
        callableStatement.setInt(1, id);
        ResultSet resultSet = callableStatement.executeQuery();
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------------+");
        System.out.printf("| %-48s | %-30s | %-30s | %-20s |%n", "Subscribed Plan", "Start Date", "End Date", "Price");
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------------+");

        while (resultSet.next()){
            String subscribedPlan = resultSet.getString("subscription_plan");
            Date startDate = resultSet.getDate("subscription_start_date");
            Date endDate = resultSet.getDate("subscription_end_date");
            double price = resultSet.getDouble("total_purchased_price");
//            System.out.print(subscribedPlan+" "+startDate+" "+endDate+" "+price);
            System.out.printf("| %-48s | %-30s | %-30s | %-20.2f |%n", subscribedPlan, startDate, endDate, price);
            System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------------+");
        }
    }

    public void dthServiceBillDetails(int id) throws SQLException{
        CallableStatement callableStatement = connection.prepareCall("{CALL GetUserDTHDetailsByID(?)}");
        callableStatement.setInt(1, id);
        ResultSet resultSet = callableStatement.executeQuery();
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------+");
        System.out.printf("| %-20s | %-40s | %-20s | %-20s | %-9s |%n", "Plan Name", "Channel Name", "Subscription Start Date", "Subscription End Date", "Price");
        System.out.println("+-------------------------------------------------------------------------------------------------------------------------------+");
        while (resultSet.next()) {
            String planName = resultSet.getString("plan_name");
            String channelName = resultSet.getString("channel_name");
            Date startDate = resultSet.getDate("subscription_start_date");
            Date endDate = resultSet.getDate("subscription_end_date");
            double channelPrice = resultSet.getDouble("channel_price");

            System.out.printf("| %-20s | %-40s | %-23s | %-20s | %-10.2f |%n", planName, channelName, startDate, endDate, channelPrice);
            System.out.println("+-------------------------------------------------------------------------------------------------------------------------------+");        }
    }

}
