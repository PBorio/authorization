package com.weblogia.authentication.controller.records;

import lombok.Builder;

@Builder
public record JwtResponseDTO(String accessToken) {
}
