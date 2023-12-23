package com.revspeed.services;

import com.revspeed.dao.UserDao;
import com.revspeed.dao.impl.UserDaoImpl;
import com.revspeed.model.User;

import java.sql.SQLException;

public class UserService {

    private UserDao userDao;
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public void registerUser(User user) throws SQLException {
        userDao.registerUser(user);
    }

    public void loginUser() throws SQLException{
        userDao.loginUser();
    }
}
