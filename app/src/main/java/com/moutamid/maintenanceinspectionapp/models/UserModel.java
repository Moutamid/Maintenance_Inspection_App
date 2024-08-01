package com.moutamid.maintenanceinspectionapp.models;

public class UserModel {
    public int userId;
    public String userNameOrEmailAddress, password, tenancyName;
    public boolean rememberClient;

    public UserModel(int userId, String userNameOrEmailAddress, String password, String tenancyName, boolean rememberClient) {
        this.userId = userId;
        this.userNameOrEmailAddress = userNameOrEmailAddress;
        this.password = password;
        this.tenancyName = tenancyName;
        this.rememberClient = rememberClient;
    }
}
