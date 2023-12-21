package com.revspeed.services;

import com.revspeed.dao.UserDao;
import com.revspeed.model.User;

public class UserService {

    private UserDao userDao;
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public void registerUser(User user){
        userDao.registerUser(user);
    }
}
