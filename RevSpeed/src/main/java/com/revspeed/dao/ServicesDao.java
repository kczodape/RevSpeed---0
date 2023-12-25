package com.revspeed.dao;

import java.sql.SQLException;

public interface ServicesDao {
    public void seeServices() throws SQLException;
    public void seeBroadbandService() throws SQLException;
    public void seeDthService() throws SQLException;
    public void seeBroadbandServiceBasedOnPlan(int id, int userId) throws SQLException;
    public void seeMyBill(int id) throws SQLException;
    public void getUsersOTTPlatforms(int id) throws SQLException;
    public void getPlansForLanguage(String language) throws SQLException;
    public void getPlansForCategory(int planId) throws SQLException;
}
