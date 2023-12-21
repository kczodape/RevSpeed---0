package com.revspeed.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

public class DBTest {

    DB db = new DB();

    @Test
    @DisplayName("Check instance of connection")
    public void testGetConnection() throws SQLException {
        Connection connection = DB.getConnection();
        assertNotNull(connection);
    }

    @Test
    @DisplayName("Check credentials of database and database name")
    public void testGetConnectionWithParameter() throws SQLException {
        String db = "RevSpeed";
        String username = "root";
        String password = "7872";

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
