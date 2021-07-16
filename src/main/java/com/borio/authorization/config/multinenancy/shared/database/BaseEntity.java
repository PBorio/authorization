package com.borio.authorization.config.multinenancy.shared.database;

import com.borio.authorization.config.multinenancy.shared.database.listeners.TenantListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public class BaseEntity implements TenantAware {

    @Size(max = 30)
    @Column(name = "tenant_id")
    private String tenantId;

    public BaseEntity(String tenantId) {
        this.tenantId = tenantId;
    }
    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
