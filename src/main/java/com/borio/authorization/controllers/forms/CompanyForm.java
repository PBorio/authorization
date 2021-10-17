package com.borio.authorization.controllers.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    //private MultipartFile logo;

}
