package org.esercizi.taskmanager.controllers;


import jakarta.validation.Valid;
import org.esercizi.taskmanager.dto.TaskCreateRequest;
import org.esercizi.taskmanager.dto.TaskResponse;
import org.esercizi.taskmanager.models.Task;
import org.esercizi.taskmanager.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
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

    @GetMapping("/{id}")
    public TaskResponse find(
            @PathVariable long id,
            Authentication authentication
    ) {
        String username = authentication.getName();
        Task task = taskService.findByIdAndUsername(id, username);
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }

    @PostMapping
    public ResponseEntity<TaskResponse> save(
            @Valid @RequestBody TaskCreateRequest taskCreateRequest,
            Authentication authentication
            )
    {
        String username = authentication.getName();
        Task task = new Task();
        task.setTitle(taskCreateRequest.title());
        task.setDescription(taskCreateRequest.description());

        Task createdTask = taskService.createTask(task, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TaskResponse(
                        createdTask.getId(),
                        createdTask.getTitle(),
                        createdTask.getDescription(),
                        createdTask.isCompleted()));
    }
}
