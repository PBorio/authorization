package com.borio.authorization.controllers.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerForm {

    private Long id;

    @NotBlank(message = "{customer.name.notempty}")
    private String name;

    private String alias;

    private String address;

    private String email;

    private String phoneNumber;

    @NotNull(message = "{customer.company.notempty}")
    private Long companyId;
}
