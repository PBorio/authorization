package com.borio.authorization.controllers.dtos;

import com.borio.authorization.domain.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;

    private String name;

    private String alias;

    private UserDto user;

    public CompanyDto(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.alias = company.getAlias();
        this.user = new UserDto(company.getUser().getId(),company.getUser().getEmail());
    }


}
