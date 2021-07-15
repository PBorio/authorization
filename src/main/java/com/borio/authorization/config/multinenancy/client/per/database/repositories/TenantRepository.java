package com.borio.authorization.config.multinenancy.client.per.database.repositories;

import com.borio.authorization.config.multinenancy.client.per.database.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, String> {

    @Query("select t from Tenant t where t.tenantId = :tenantId")
    Optional<Tenant> findByTenantId(String tenantId);

}
