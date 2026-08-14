package org.esercizi.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.esercizi.taskmanager.models.User;

public record TaskCreateRequest(
        @NotBlank
        String title,

        boolean completed,

        @NotNull
        User owner
) {

}
