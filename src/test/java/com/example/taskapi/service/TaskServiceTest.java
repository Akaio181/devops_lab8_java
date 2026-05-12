package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(Task.TaskStatus.TODO)
                .build();
    }

    @Test
    @DisplayName("getAllTasks returns all tasks from repository")
    void getAllTasks_returnsAll() {
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));

        List<Task> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("getTaskById returns task when exists")
    void getTaskById_found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Optional<Task> result = taskService.getTaskById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getTaskById returns empty when not found")
    void getTaskById_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("createTask saves and returns new task")
    void createTask_savesAndReturns() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task created = taskService.createTask(sampleTask);

        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getTitle()).isEqualTo("Test Task");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask updates fields when task exists")
    void updateTask_updatesWhenExists() {
        Task updated = Task.builder()
                .title("Updated")
                .description("Updated desc")
                .status(Task.TaskStatus.DONE)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Task> result = taskService.updateTask(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Updated");
        assertThat(result.get().getStatus()).isEqualTo(Task.TaskStatus.DONE);
    }

    @Test
    @DisplayName("updateTask returns empty when task does not exist")
    void updateTask_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.updateTask(99L, sampleTask);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deleteTask returns true when task exists")
    void deleteTask_existingTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        boolean deleted = taskService.deleteTask(1L);

        assertThat(deleted).isTrue();
        verify(taskRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteTask returns false when task does not exist")
    void deleteTask_nonExistingTask() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        boolean deleted = taskService.deleteTask(99L);

        assertThat(deleted).isFalse();
    }

    @Test
    @DisplayName("getTasksByStatus filters by status")
    void getTasksByStatus_filtersCorrectly() {
        when(taskRepository.findByStatus(Task.TaskStatus.TODO))
                .thenReturn(List.of(sampleTask));

        List<Task> tasks = taskService.getTasksByStatus(Task.TaskStatus.TODO);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getStatus()).isEqualTo(Task.TaskStatus.TODO);
    }
}
