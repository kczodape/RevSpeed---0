package com.revspeed.services;

import com.revspeed.dao.UserDao;
import com.revspeed.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {
    @Test
    void testRegisterUser() throws SQLException {
        // Arrange
        UserDao mockedUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockedUserDao);

        User userToRegister = new User(); // Create a user instance for testing

        // Act
        userService.registerUser(userToRegister);

        // Assert
        Mockito.verify(mockedUserDao, Mockito.times(1)).registerUser(any(User.class));
        // Add more assertions based on the expected behavior after user registration
    }

    @Test
    void testLoginUser() throws SQLException{
//        Arrange
        UserDao mockedUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockedUserDao);
        User userToLogin = new User();

//        Act
        userService.loginUser();

//        Assert
        Mockito.verify(mockedUserDao, Mockito.times(1)).loginUser();
    }

}
