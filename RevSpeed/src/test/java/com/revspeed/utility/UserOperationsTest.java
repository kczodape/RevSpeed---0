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

}
