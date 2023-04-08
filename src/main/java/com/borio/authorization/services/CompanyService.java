package com.borio.authorization.services;

import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.controllers.forms.LogoForm;
import com.borio.authorization.domain.Company;

public interface CompanyService {
    Company create(CompanyForm any);

    Company findById(Long id);

    Company addLogo(LogoForm logoForm);
}
