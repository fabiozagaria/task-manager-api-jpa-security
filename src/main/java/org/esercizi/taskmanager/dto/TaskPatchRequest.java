package org.esercizi.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskPatchRequest(

        String title,
        String description,
        Boolean completed
) {
}
