package com.borio.authorization.config.multinenancy.client.per.database.model;

import liquibase.pro.packaged.T;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
public class Tenant {

    @Id
    @Size(max = 30)
    @Column(name = "tenant_id")
    private String tenantId;

    @Size(max = 30)
    private String db;

    @Size(max = 30)
    private String password;

    @Size(max = 256)
    private String url;

    public static Tenant bootstrap() {
        Tenant t = new Tenant();
        t.setDb("bootstrap");
        t.setPassword("123456");
        t.setUrl("jdbc:h2:mem:bootstrap");
        return t;
    }
}
