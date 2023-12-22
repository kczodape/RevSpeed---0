package com.revspeed.services;

import com.revspeed.dao.UserDao;
import com.revspeed.dao.impl.UserDaoImpl;
import com.revspeed.model.User;

public class UserService {

    private UserDao userDao = new UserDaoImpl();

    public void registerUser(User user){
        userDao.registerUser(user);
    }
}
