package org.esercizi.taskmanager.dto;

public record TaskResponse(
        Long id,
        String title,
        boolean completeted,
        UserResponse userResponse
) {
}
