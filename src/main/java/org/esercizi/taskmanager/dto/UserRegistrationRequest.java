package org.esercizi.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank
        String username,
        @NotBlank
        @Size(min = 8)
        String password
) {
}
