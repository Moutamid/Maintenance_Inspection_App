package com.moutamid.maintenanceinspectionapp.models;

public class TenantModel {
    public String tenancyName, name;
    public boolean isActive;
    public int id;

    public TenantModel(String tenancyName, String name, boolean isActive, int id) {
        this.tenancyName = tenancyName;
        this.name = name;
        this.isActive = isActive;
        this.id = id;
    }
}
