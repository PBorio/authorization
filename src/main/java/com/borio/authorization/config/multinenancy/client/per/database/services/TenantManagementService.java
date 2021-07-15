package com.borio.authorization.config.multinenancy.client.per.database.services;


public interface TenantManagementService {

    void createTenant(String tenantId, String db, String password);



}
