package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.CompanyDto;
import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyDto> createCompany(@Valid @RequestBody CompanyForm companyForm,
                                               UriComponentsBuilder uriComponentsBuilder) {

        Company createdCompany = this.companyService.create(companyForm);
        CompanyDto companyDto = new CompanyDto(createdCompany);

        URI uri = uriComponentsBuilder.path("/company/{id}").buildAndExpand(companyDto.getId()).toUri();

        return ResponseEntity.created(uri).body(companyDto);
    }

}
