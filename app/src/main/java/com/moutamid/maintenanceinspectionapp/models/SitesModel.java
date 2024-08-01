package com.moutamid.maintenanceinspectionapp.models;

public class SitesModel {
    public String siteName;
    public TenantModel tenant;
    public int tenantId;
    public String id;

    public SitesModel(String siteName, TenantModel tenant, int tenantId, String id) {
        this.siteName = siteName;
        this.tenant = tenant;
        this.tenantId = tenantId;
        this.id = id;
    }
}
