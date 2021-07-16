package com.borio.authorization.config.multinenancy.shared.database;

public interface TenantAware {
    void setTenantId(String tenantId);
}
