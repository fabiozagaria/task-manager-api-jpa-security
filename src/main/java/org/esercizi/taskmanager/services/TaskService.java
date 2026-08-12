package org.esercizi.taskmanager.services;

import org.esercizi.taskmanager.models.Task;
import org.esercizi.taskmanager.models.User;
import org.esercizi.taskmanager.repository.TaskRepository;
import org.esercizi.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

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
              .orElseThrow();

    }

    public Task createTask(Task task, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow();
        task.setOwner(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask, String username) {
        Task task = taskRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow();
        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        task.setDescription(updatedTask.getDescription());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow();
        taskRepository.delete(task);
    }

}
