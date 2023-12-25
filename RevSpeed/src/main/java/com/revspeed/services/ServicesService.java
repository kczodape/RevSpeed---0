package com.revspeed.services;

import com.revspeed.dao.ServicesDao;
import com.revspeed.dao.UserDao;

import java.sql.SQLException;

public class ServicesService {
    private ServicesDao servicesDao;
    public ServicesService(ServicesDao servicesDao){
        this.servicesDao = servicesDao;
    }
    public void seeServices() throws SQLException{
        servicesDao.seeServices();
    }

    public void seeMyBill(int id) throws SQLException{
        servicesDao.seeMyBill(id);
    }

    public void getUsersOTTPlatforms(int id) throws SQLException{
        servicesDao.getUsersOTTPlatforms(id);
    }
}
