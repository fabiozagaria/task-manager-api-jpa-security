package org.esercizi.taskmanager.dto;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {
}
