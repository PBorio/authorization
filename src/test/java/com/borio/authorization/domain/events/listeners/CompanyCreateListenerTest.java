package com.borio.authorization.domain.events.listeners;

import com.borio.authorization.config.multinenancy.client.per.database.services.TenantManagementService;
import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.User;
import com.borio.authorization.domain.events.OnCompanyCreatedEvent;
import com.borio.authorization.domain.events.OnRegistrationCompleteEvent;
import com.borio.authorization.services.RegisterService;
import liquibase.pro.packaged.U;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CompanyCreateListenerTest {

    @Mock
    TenantManagementService tenantManagementService;

    @InjectMocks
    CompanyCreateListener companyCreateListener = new CompanyCreateListener();

    @Test
    public void onApplicationEventShouldCreateANewTenantDBForTheCompany() {

        Company company = new Company();
        company.setUser(new User());
        company.setAlias("tenant_test");
        company.setName("A Company Name");

        companyCreateListener.onApplicationEvent(new OnCompanyCreatedEvent(company, Locale.GERMANY, ""));
        verify(tenantManagementService,times(1)).createTenant(company.getAlias(),
                                                                                    company.getAlias(),
                                                                                    company.getAlias());
    }

}
