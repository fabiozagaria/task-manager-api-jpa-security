package org.esercizi.taskmanager.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
