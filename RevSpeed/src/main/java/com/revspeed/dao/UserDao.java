package com.revspeed.dao;

import com.revspeed.model.User;

import java.sql.SQLException;

public interface UserDao {
    public void registerUser(User user) throws SQLException;
    public void loginUser() throws SQLException;
}
