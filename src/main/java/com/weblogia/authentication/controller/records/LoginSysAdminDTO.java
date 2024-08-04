package com.weblogia.authentication.controller.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginSysAdminDTO (
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 20, message = "Username must be between 3 and 50 characters")
        String password,

        @NotNull(message = "Company Id cannot be null")
        Long companyId){
}
