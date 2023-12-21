package com.revspeed.model;

public class User {

    private String name;
    private long phoneNumber;
    private String address;
    private String emailId;
    private String Password;
    private boolean isBoolean;
    private boolean isDth;

    public User(){}

    public User(String name, long phoneNumber, String address, String emailId, String password, boolean isBoolean, boolean isDth) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailId = emailId;
        Password = password;
        this.isBoolean = isBoolean;
        this.isDth = isDth;
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
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }

    public boolean isDth() {
        return isDth;
    }

    public void setDth(boolean dth) {
        isDth = dth;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'' +
                ", emailId='" + emailId + '\'' +
                ", Password='" + Password + '\'' +
                ", isBoolean=" + isBoolean +
                ", isDth=" + isDth +
                '}';
    }
}
