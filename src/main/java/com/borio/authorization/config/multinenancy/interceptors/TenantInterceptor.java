package com.borio.authorization.config.multinenancy.interceptors;

import com.borio.authorization.config.multinenancy.TenantContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
public class TenantInterceptor implements WebRequestInterceptor {


    @Override
    public void preHandle(WebRequest request) {
        String tenantId = "BOOTSTRAP";
        if (request.getHeader("X-TENANT-ID") != null) {
            tenantId = request.getHeader("X-TENANT-ID");
        }
        TenantContext.setTenantId(tenantId);
    }

    @Override
    public void postHandle(WebRequest webRequest, ModelMap modelMap) {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(WebRequest webRequest,
                                Exception e) {

    }
}
