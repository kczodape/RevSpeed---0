package com.revspeed.dao.impl;

import com.revspeed.dao.UserDao;
import com.revspeed.model.User;
import com.revspeed.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.*;
import java.sql.*;
import java.sql.Types;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDaoImplTest {

    private InputStream originalSystemIn;

    @BeforeEach
    void setUp() {
        // Save the original System.in
        originalSystemIn = System.in;
    }

    @AfterEach
    void tearDown() {
        // Reset System.in after each test
        System.setIn(originalSystemIn);
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() throws SQLException {
        // Arrange
        Connection mockedConnection = mock(Connection.class);
        CallableStatement mockedCallableStatement = mock(CallableStatement.class);

        when(mockedConnection.prepareCall(any())).thenReturn(mockedCallableStatement);

        // Mock user input
        String userInput = "John Doe\n1234567890\nSample Address\njohn@example.com\npassword\n";
        InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inputStream);

        UserDaoImpl userDao = new UserDaoImpl(mockedConnection);

        // Act
        assertDoesNotThrow(() -> userDao.registerUser(new User()));

        // Assert
        verify(mockedCallableStatement).setString(eq(1), eq("John Doe"));
        verify(mockedCallableStatement).setLong(eq(2), eq(1234567890L));
        verify(mockedCallableStatement).setString(eq(3), eq("Sample Address"));
        verify(mockedCallableStatement).setString(eq(4), eq("john@example.com"));
        verify(mockedCallableStatement).setString(eq(5), eq("password"));

        verify(mockedCallableStatement).executeUpdate();
        // Reset System.in after the test
        System.setIn(System.in);
    }

    @Test
    void testLoginUser_SuccessfulLogin() throws SQLException, IOException {
        // Arrange
        Connection mockedConnection = mock(Connection.class);
        CallableStatement mockedCallableStatement = mock(CallableStatement.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        // Mock user input
        String userInput = "john@example.com\npassword\n";
        InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());

        when(mockedConnection.prepareCall(any())).thenReturn(mockedCallableStatement);
        when(mockedCallableStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true);
        when(mockedResultSet.getInt("success")).thenReturn(1);

        // Save the original System.in
//        InputStream originalSystemIn = System.in;
        // Create a ByteArrayOutputStream to capture the printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

//        try {
            // Set the custom InputStream
//            System.setIn(inputStream);

            UserDaoImpl userDao = new UserDaoImpl(mockedConnection, inputStream);

            // Act
            assertDoesNotThrow(() -> userDao.loginUser());

            // Assert
            verify(mockedCallableStatement).setString(eq(1), eq("john@example.com"));
            verify(mockedCallableStatement).setString(eq(2), eq("password"));

            verify(mockedCallableStatement).executeQuery();
        assertFalse(outputStream.toString().contains("Invalid email or password. Please try again."),
                "Unexpected a failure message for successful login. Actual output:\n" + outputStream.toString());

        inputStream.close();
//        } finally {
//            // Reset System.in after the test
//            System.setIn(originalSystemIn);
//            inputStream.close();
//        }
    }


    @Test
    void testisEmailExist() throws SQLException{
//        Arrange
        Connection mockConnection = mock(Connection.class);
        CallableStatement mockedCallableStatement = mock(CallableStatement.class);

        when(mockConnection.prepareCall(anyString())).thenReturn(mockedCallableStatement);
        when(mockedCallableStatement.execute()).thenReturn(true);
        when(mockedCallableStatement.getInt(2)).thenReturn(1);

        UserDaoImpl userDao = new UserDaoImpl(mockConnection);

//        Act
        boolean result = userDao.isEmailExist("checkemail@gmail.com");

//        Assert
        assertTrue(result);

//   // Verify interaction
    verify(mockedCallableStatement).setString(eq(1), eq("checkemail@gmail.com"));
    verify(mockedCallableStatement).registerOutParameter(eq(2), eq(Types.INTEGER));
    verify(mockedCallableStatement).execute();
    }


    @Test
    void testIsEmailValid_ValidEmail() throws SQLException{
        // Arrange
        UserDaoImpl userDao = new UserDaoImpl(null); // You may pass a connection or mock it
        String validEmail = "user@example.com";

        // Act
        boolean result = userDao.isEmailValid(validEmail);

        // Assert
        assertTrue(result, "Expected the email to be valid");
    }

    @Test
    void testIsEmailValid_InvalidEmail() throws SQLException{
        // Arrange
        UserDaoImpl userDao = new UserDaoImpl(null); // You may pass a connection or mock it
        String invalidEmail = "invalid_email";

        // Act
        boolean result = userDao.isEmailValid(invalidEmail);

        // Assert
        assertFalse(result, "Expected the email to be invalid");
    }

}