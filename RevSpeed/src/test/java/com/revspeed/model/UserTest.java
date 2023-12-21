package com.revspeed.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user = new User();

    @Test
    @DisplayName("Create instance of user class")
    public void testUserClassInstance(){
        assertNotNull(user);
    }

    @Test
    @DisplayName("Create test for parameterized cunstroctor")
    public void testParameterizedConstruction(){
         String name = "Krunal";
         long phoneNumber = 9876543210L;
         String address = "Manali";
         String emailId = "Kr@gmail.com";
         String password = "1234";

         User user = new User(name, phoneNumber, address, emailId, password);

         assertEquals(name, user.getName());
         assertEquals(phoneNumber, user.getPhoneNumber());
         assertEquals(address, user.getAddress());
         assertEquals(emailId, user.getEmailId());
         assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("Test case for setName()")
    public void testSetName(){
        String name = "Krunal";
        user.setName(name);
        assertEquals(name, user.getName());
    }@Test
    @DisplayName("Test case for setPhoneNumbere()")
    public void testsetPhoneNumbere(){
        long phoneNumber = 9876543210L;
        user.setPhoneNumber(phoneNumber);
        assertEquals(phoneNumber, user.getPhoneNumber());
    }@Test
    @DisplayName("Test case for setAddress()")
    public void testsetAddress(){
        String address = "Manali";
        user.setAddress(address);
        assertEquals(address, user.getAddress());
    }@Test
    @DisplayName("Test case for setEmailId()")
    public void testsetEmailId(){
        String emailId = "Kr@gmail.com";
        user.setEmailId(emailId);
        assertEquals(emailId, user.getEmailId());
    }@Test
    @DisplayName("Test case for setPassword()")
    public void testsetPassword(){
        String password = "1234";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

}
