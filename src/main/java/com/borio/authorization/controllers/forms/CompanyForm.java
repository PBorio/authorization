package com.borio.authorization.controllers.forms;

import com.borio.authorization.controllers.dtos.UserDto;
import com.borio.authorization.domain.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyForm {

    private Long id;

    @NotBlank(message = "{company.name.notempty}")
    private String name;

    @NotBlank(message = "{company.alias.notempty}")
    @Pattern(regexp="[A-Za-z0-9_]*",message="{company.alias.wrong}")
    private String alias;

    @NotNull(message = "{company.user.null}")
    private Long userId;

}
