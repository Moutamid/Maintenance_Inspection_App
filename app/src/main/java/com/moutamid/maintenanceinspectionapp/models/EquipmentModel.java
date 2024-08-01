package com.moutamid.maintenanceinspectionapp.models;

public class EquipmentModel {

    public String id, locationId, serialNumber, description;
    public boolean isActive;

    public EquipmentModel(String id, String locationId, String serialNumber, String description, boolean isActive) {
        this.id = id;
        this.locationId = locationId;
        this.serialNumber = serialNumber;
        this.description = description;
        this.isActive = isActive;
    }
}
