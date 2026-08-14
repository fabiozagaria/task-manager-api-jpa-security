package org.esercizi.taskmanager.controllers;


import org.esercizi.taskmanager.dto.TaskResponse;
import org.esercizi.taskmanager.models.Task;
import org.esercizi.taskmanager.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> findAll(
            Authentication authentication
    ) {
        String username = authentication.getName();
        List<Task> listTask = taskService.findAllByUsername(username);
        return listTask.stream()
                .map(
                        task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted())
                ).toList();
    }
}
