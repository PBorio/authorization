package com.weblogia.authentication.repositories;

import com.weblogia.authentication.model.ApplicationPermission;
import com.weblogia.authentication.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationPermissionRepository extends JpaRepository<ApplicationPermission, Long> {
    List<ApplicationPermission> findByCompany(Company company);
}
