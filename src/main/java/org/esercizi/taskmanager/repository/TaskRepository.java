package org.esercizi.taskmanager.repository;

import org.esercizi.taskmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findAllByOwnerUsername(String username);

    Optional<Task> findByIdAndOwnerUsername(Long id, String username);
}
