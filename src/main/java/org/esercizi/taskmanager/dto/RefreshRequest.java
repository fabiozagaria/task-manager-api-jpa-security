package org.esercizi.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank
        String refreshToken,
        @NotBlank
        String accessToken
) {
}
