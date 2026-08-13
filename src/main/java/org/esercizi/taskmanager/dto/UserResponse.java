package org.esercizi.taskmanager.dto;

public record UserResponse(
        Long id,
        String username,
        String role
) {
}
