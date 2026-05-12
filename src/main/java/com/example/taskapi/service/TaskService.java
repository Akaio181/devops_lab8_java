package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        log.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(final Long id) {
        log.info("Fetching task with id={}", id);
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByStatus(final Task.TaskStatus status) {
        log.info("Fetching tasks with status={}", status);
        return taskRepository.findByStatus(status);
    }

    public Task createTask(final Task task) {
        log.info("Creating task: title={}", task.getTitle());
        task.setCreatedAt(LocalDateTime.now());
        Task saved = taskRepository.save(task);
        log.info("Task created with id={}", saved.getId());
        return saved;
    }

    public Optional<Task> updateTask(final Long id, final Task updatedTask) {
        log.info("Updating task id={}", id);
        return taskRepository.findById(id).map(existing -> {
            existing.setTitle(updatedTask.getTitle());
            existing.setDescription(updatedTask.getDescription());
            existing.setStatus(updatedTask.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            Task saved = taskRepository.save(existing);
            log.info("Task id={} updated successfully", id);
            return saved;
        });
    }

    public boolean deleteTask(final Long id) {
        log.info("Deleting task id={}", id);
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            log.info("Task id={} deleted", id);
            return true;
        }
        log.warn("Task id={} not found for deletion", id);
        return false;
    }
}
