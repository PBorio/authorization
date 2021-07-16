package com.borio.authorization.domain.events.listeners;

import com.borio.authorization.config.multinenancy.client.per.database.services.TenantManagementService;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.events.OnCompanyCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CompanyCreateListener implements ApplicationListener<OnCompanyCreatedEvent> {

    @Autowired
    TenantManagementService tenantManagementService;

    @Override
    public void onApplicationEvent(OnCompanyCreatedEvent event) {
        Company company = event.getCompany();
        tenantManagementService.createTenant(company.getAlias(), company.getAlias(), company.getAlias());
    }
}
