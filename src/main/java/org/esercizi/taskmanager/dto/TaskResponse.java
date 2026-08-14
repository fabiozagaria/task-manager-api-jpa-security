package org.esercizi.taskmanager.dto;

public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean completed



) {
}
