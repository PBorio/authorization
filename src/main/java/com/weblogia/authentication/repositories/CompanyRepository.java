package com.weblogia.authentication.repositories;

import com.weblogia.authentication.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByName(String companyName);
}
