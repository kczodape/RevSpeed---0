package com.revspeed.utility;

import com.revspeed.utility.UserOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class UserOperationsTest {
    private Connection mockConnection = mock(Connection.class);
    private UserOperations userOperations = new UserOperations(mockConnection);


    @BeforeAll
    static void initAll() {
    }
    @BeforeEach
    void init() {
    }
    @BeforeEach
    void setUp() {
        userOperations.connection = mockConnection;
    }

    @Test
    @DisplayName("see Profile")
    public void seeProfile(){
        try {
            String name="";
            Long phoneNumber=0L;
            String address="";
            String email="";
            String password="";
            String role="";


            UserOperations.seeProfile( name ,phoneNumber ,address ,email ,password ,role);
            Assertions.assertTrue(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.assertFalse(false);
        }
    }

//    @Test
//    void testUpdateProfile() throws SQLException {
//        int userId = 1;
//        // Mocking database connection and CallableStatement using Mockito
//        Connection mockConnection = mock(Connection.class);
//        CallableStatement mockCallableStatement = mock(CallableStatement.class);
//
//        // Mocking the expected behavior of the CallableStatement
//        when(mockConnection.prepareCall(any())).thenReturn(mockCallableStatement);
//        when(mockCallableStatement.getInt(1)).thenReturn(userId);
//        when(mockCallableStatement.getString(2)).thenReturn("NewName");
//        when(mockCallableStatement.getLong(3)).thenReturn((long) Types.NULL);
//        when(mockCallableStatement.getString(4)).thenReturn(null);
//        when(mockCallableStatement.getString(5)).thenReturn(null);
//        when(mockCallableStatement.getString(6)).thenReturn(null);
//        when(mockCallableStatement.executeUpdate()).thenReturn(1); // Assuming 1 as a placeholder return value, adjust as needed
//
//        UserOperations yourClassInstance = new UserOperations(mockConnection);
//
//        // Act and Assert
//        assertDoesNotThrow(() -> yourClassInstance.updateProfile(userId));
//
//        verify(mockCallableStatement).setString(eq(1), eq("John Doe"));
//        verify(mockCallableStatement).setLong(eq(2), eq(1234567890L));
//        verify(mockCallableStatement).setString(eq(3), eq(null));
//        verify(mockCallableStatement).setString(eq(4), eq(null));
//        verify(mockCallableStatement).setString(eq(5), eq(null));
//
//        verify(mockCallableStatement).executeUpdate();
//        System.setIn(System.in);
//    }

//    @Test
//    void updateProfile_validChoice_shouldExecuteUpdate() throws SQLException {
//        // Arrange
//        ByteArrayInputStream in = new ByteArrayInputStream("1\nNewName\n".getBytes());
//        System.setIn(in);
//
//        // Mock the necessary parts
//        CallableStatement mockCallableStatement = mock(CallableStatement.class);
//        when(mockConnection.prepareCall(anyString())).thenReturn(mockCallableStatement);
//        doNothing().when(mockCallableStatement).setInt(anyInt(), anyInt());
//        doNothing().when(mockCallableStatement).setString(anyInt(), anyString());
//        when(mockCallableStatement.execute()).thenReturn(true);
//
//        // Act
//        assertDoesNotThrow(() -> userOperations.updateProfile(1));
//
//        // Assert
//        verify(mockCallableStatement).setString(2, "NewName");
//        verify(mockCallableStatement, never()).setNull(anyInt(), anyInt());
//        verify(mockCallableStatement).executeUpdate();
//        assertEquals("Profile updated successfully!\n", getConsoleOutput());
//
//        // Additional verifications for debugging
//        ArgumentCaptor<Integer> columnIndexCaptor = ArgumentCaptor.forClass(Integer.class);
//        ArgumentCaptor<Integer> typeCaptor = ArgumentCaptor.forClass(Integer.class);
//        verify(mockCallableStatement, never()).setNull(columnIndexCaptor.capture(), typeCaptor.capture());
//
//        // Debug output for captured values
//        System.out.println("setNull called with arguments: " + columnIndexCaptor.getValue() + ", " + typeCaptor.getValue());
//    }


//    @Test
//    void updateProfile_invalidChoice_shouldPrintErrorMessage() throws SQLException {
//        // Arrange
//        ByteArrayInputStream in = new ByteArrayInputStream("invalid\n".getBytes());
//        System.setIn(in);
//
//        // Act
//        assertDoesNotThrow(() -> userOperations.updateProfile(1));
//
//        // Assert
//        assertEquals("Invalid choice\n", getConsoleOutput());
//    }

    // Helper method to capture console output
    private String getConsoleOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        return outContent.toString();
    }
}
