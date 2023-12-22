package com.revspeed.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ResourceBundle;

public class DBTest {

    @Test
    @DisplayName("Check instance of connection")
    public void testGetConnection() throws SQLException {
        Connection connection = DB.getConnection();
        assertNotNull(connection);
    }

    @Test
    @DisplayName("Check credentials of database and database name")
    public void testGetConnectionWithParameter() throws SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("sql");
        String db = resourceBundle.getString("db");
        String username = resourceBundle.getString("username");
        String password = resourceBundle.getString("password");

        Connection connection = DB.getConnection(db, username, password);

        assertNotNull(connection);
    }

    @Test
    @DisplayName("Throw SQLException for wrong user, pass and db ane")
    void testSQLExceptionForInvalidCredentials() {
        // Act and Assert
        assertThrows(SQLException.class, () ->
                DB.getConnection("invalidDB", "invalidUser", "invalidPassword"));
    }

}