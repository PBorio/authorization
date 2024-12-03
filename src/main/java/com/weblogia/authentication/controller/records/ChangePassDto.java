package com.weblogia.authentication.controller.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePassDto(


        @NotBlank(message = "New Password is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String currentPassword,

        @NotBlank(message = "New Password is required")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        String newPassword
) {}
