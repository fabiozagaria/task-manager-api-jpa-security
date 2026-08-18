package org.esercizi.taskmanager.controllers;


import jakarta.validation.Valid;
import org.esercizi.taskmanager.dto.TaskCreateRequest;
import org.esercizi.taskmanager.dto.TaskPatchRequest;
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
                .body(taskService.toTaskReponse(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> put(
          @PathVariable long id,
          Authentication authentication,
          @Valid @RequestBody Task taskUpdated
    ) {
        String username = authentication.getName();

        Task newTask = taskService.updateTask(id, taskUpdated ,username);
        return ResponseEntity
                .ok(taskService.toTaskReponse(newTask));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> patch(
            @PathVariable long id,
            Authentication authentication,
            @RequestBody TaskPatchRequest taskPatchRequest
            ) {
        String username = authentication.getName();
        Task taskUpdated = taskService.patchTask(id, taskPatchRequest, username);
        return ResponseEntity
                .ok(taskService.toTaskReponse(taskUpdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id,
            Authentication authentication
    ) {
        String username = authentication.getName();
        taskService.deleteTask(id, username);
        return ResponseEntity
                .noContent()
                .build();
    }
}
