package org.esercizi.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;
import org.esercizi.taskmanager.models.User;

public record TaskCreateRequest(
        @NotBlank
        String title,
        String description
) {

}
