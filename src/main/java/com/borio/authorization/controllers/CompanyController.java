package com.borio.authorization.controllers;

import com.borio.authorization.controllers.dtos.CompanyDto;
import com.borio.authorization.controllers.forms.CompanyForm;
import com.borio.authorization.domain.Company;
import com.borio.authorization.services.CompanyService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CompanyDto> createCompany(@Valid @ModelAttribute CompanyForm companyForm,
                                               UriComponentsBuilder uriComponentsBuilder) {

        Company createdCompany = this.companyService.create(companyForm);
        CompanyDto companyDto = new CompanyDto(createdCompany);

        URI uri = uriComponentsBuilder.path("/company/{id}").buildAndExpand(companyDto.getId()).toUri();

        return ResponseEntity.created(uri).body(companyDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Long id) {

        Company company = this.companyService.findById(id);
        CompanyDto companyDto = new CompanyDto(company);

        return ResponseEntity.ok().body(companyDto);
    }

}
