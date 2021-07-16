package com.borio.authorization.domain.events;

import com.borio.authorization.domain.Company;
import com.borio.authorization.domain.User;
import org.springframework.context.ApplicationEvent;

import java.util.Date;
import java.util.Locale;

public class OnCompanyCreatedEvent extends ApplicationEvent {

    private final Company company;
    private final Locale locale;
    private final String appUrl;

    public OnCompanyCreatedEvent(Company company, Locale locale, String appUrl) {
        super(company);
        this.company = company;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public Company getCompany() {
        return company;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
