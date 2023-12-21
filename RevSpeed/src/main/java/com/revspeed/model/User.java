package com.revspeed.model;

public class User {

    private String name;
    private long phoneNumber;
    private String address;
    private String emailId;
    private String password;

    public User(){}

    public User(String name, long phoneNumber, String address, String emailId, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailId = emailId;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
