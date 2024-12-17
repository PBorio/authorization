package com.weblogia.authentication.controller.records;

import lombok.Builder;

import java.util.List;

@Builder
public record JwtResponseDTO(String accessToken, List<String> roles) {
}
