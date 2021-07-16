package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.domain.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {
    @Override
    public Company create(CompanyForm any) {
        return null;
    }
}
