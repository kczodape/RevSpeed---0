package com.revspeed.dao.impl;

import com.revspeed.dao.UserDao;
import com.revspeed.model.User;
import com.revspeed.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.*;
import java.sql.Types;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDaoImplTest {

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
    void testisEmailExist() throws SQLException{
//        Arrange
        Connection mockConnection = mock(Connection.class);
        CallableStatement mockedCallableStatement = mock(CallableStatement.class);

        when(mockConnection.prepareCall(anyString())).thenReturn(mockedCallableStatement);
        when(mockedCallableStatement.getInt(2)).thenReturn(1);

        UserDaoImpl userDao = new UserDaoImpl(mockConnection);

//        Act
        boolean result = userDao.isEmailExist("checkemail@gmail.com");

//        Assert
        assertTrue(result);

//        Verify interaction
        when(mockedCallableStatement.getString(1)).thenReturn("checkemail@gmail.com");
        doNothing().when(mockedCallableStatement).registerOutParameter(2, Types.INTEGER);
        when(mockedCallableStatement.execute()).thenReturn(true);
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
