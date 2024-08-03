package com.moutamid.maintenanceinspectionapp.models;

public class LocationModel {
    public String description, id, siteId;

    public LocationModel(String description, String id, String siteId) {
        this.description = description;
        this.id = id;
        this.siteId = siteId;
    }
}
