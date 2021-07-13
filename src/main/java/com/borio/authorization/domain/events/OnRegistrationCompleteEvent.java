package com.borio.authorization.domain.events;

import com.borio.authorization.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.util.Date;
import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {


    private final User user;
    private final Locale locale;
    private final String appUrl;
    private final Date createTime;

    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl, Date createTime) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
