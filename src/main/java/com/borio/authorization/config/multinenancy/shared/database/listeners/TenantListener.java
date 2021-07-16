package com.borio.authorization.config.multinenancy.shared.database.listeners;

import com.borio.authorization.config.multinenancy.TenantContext;
import com.borio.authorization.config.multinenancy.shared.database.TenantAware;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TenantListener {

    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(TenantAware entity) {
        final String tenantId = TenantContext.getTenantId();
        entity.setTenantId(tenantId);
    }

}
