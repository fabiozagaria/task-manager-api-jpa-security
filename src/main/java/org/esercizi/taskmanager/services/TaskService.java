package org.esercizi.taskmanager.services;

import org.esercizi.taskmanager.dto.TaskPatchRequest;
import org.esercizi.taskmanager.dto.TaskResponse;
import org.esercizi.taskmanager.exceptions.TaskNotFoundException;
import org.esercizi.taskmanager.models.Task;
import org.esercizi.taskmanager.models.User;
import org.esercizi.taskmanager.repository.TaskRepository;
import org.esercizi.taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.method.MethodValidationException;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> findAllByUsername(String username) {
        return taskRepository.findAllByOwnerUsername(username);
    }

    public Task findByIdAndUsername(Long id, String username) {
        return taskRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

    }

    public Task createTask(Task task, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        task.setOwner(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask, String username) {
        Task task = taskRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        task.setDescription(updatedTask.getDescription());

        return taskRepository.save(task);
    }

    public Task patchTask(Long id, TaskPatchRequest taskPatchRequest, String username) {

        String title = taskPatchRequest.title();
        String description = taskPatchRequest.description();
        Boolean completed = taskPatchRequest.completed();


        Task task = taskRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
            if (title != null) {
                    if (title.isBlank()) {
                        throw new IllegalArgumentException("Titolo vuoto");
                    }
                String titleClean = title.trim();
                task.setTitle(titleClean);
            }

            if (description != null) {
                if (description.isBlank()) {
                    throw new IllegalArgumentException("Descrizione vuota");
                }
                String descriptionClean = description.trim();
                task.setDescription(descriptionClean);
            }

            if (completed != null) {
                task.setCompleted(completed);

            }

            return taskRepository.save(task);

        }

        public void deleteTask (Long id, String username){
            Task task = taskRepository.findByIdAndOwnerUsername(id, username)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found"));
            taskRepository.delete(task);
        }

        public TaskResponse toTaskReponse(Task task) {
            return new TaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.isCompleted()
            );
        }


    }

