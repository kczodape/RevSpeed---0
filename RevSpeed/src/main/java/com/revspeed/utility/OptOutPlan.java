package com.revspeed.utility;

import com.revspeed.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OptOutPlan {
    private static final Logger logger = LoggerFactory.getLogger(DB.class);
    static Connection connection;

    static {
        try {
            connection = DB.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void optOutPlan(String todaysDate) throws SQLException {
        String deactivateUserQuerry = "update user_service_link set user_status = 0 where subscription_end_date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deactivateUserQuerry);
        preparedStatement.setString(1, todaysDate);
        preparedStatement.executeUpdate();
        System.out.println("plan opt out successfull");
        logger.info("Plan opt out of user");
    }
}
