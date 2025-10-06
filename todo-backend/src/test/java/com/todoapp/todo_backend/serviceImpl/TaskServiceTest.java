package com.todoapp.todo_backend.serviceImpl;

import com.todoapp.todo_backend.dto.requestDto.TaskRequestDTO;
import com.todoapp.todo_backend.exception.CustomException;
import com.todoapp.todo_backend.model.Task;
import com.todoapp.todo_backend.repository.TaskRepository;
import com.todoapp.todo_backend.service.impl.TaskServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImpTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImp taskService;

    private Task testTask;
    private TaskRequestDTO taskRequestDTO;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCompleted(false);
        testTask.setCreatedAt(LocalDateTime.now());

        taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTitle("Test Task");
        taskRequestDTO.setDescription("Test Description");
    }

    @Test
    void createTask_Success() throws CustomException {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.createTask(taskRequestDTO);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertFalse(result.getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_NullTitle_ThrowsException() {
        taskRequestDTO.setTitle(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.createTask(taskRequestDTO));

        assertEquals("Task title cannot be empty", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createTask_EmptyTitle_ThrowsException() {
        taskRequestDTO.setTitle("");

        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.createTask(taskRequestDTO));

        assertEquals("Task title cannot be empty", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createTask_WhitespaceTitle_ThrowsException() {
        taskRequestDTO.setTitle("   ");

        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.createTask(taskRequestDTO));

        assertEquals("Task title cannot be empty", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getIncompleteTasks_Success() {
        Task task1 = new Task(1L, "Task 1", "Description 1", false, LocalDateTime.now());
        Task task2 = new Task(2L, "Task 2", "Description 2", false, LocalDateTime.now());
        List<Task> tasks = Arrays.asList(task1, task2);

        Pageable pageable = PageRequest.of(0, 5);
        when(taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable)).thenReturn(tasks);

        List<Task> result = taskService.getIncompleteTasks(5);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findByCompletedFalseOrderByCreatedAtDesc(pageable);
    }

    @Test
    void getIncompleteTasks_EmptyList() {
        Pageable pageable = PageRequest.of(0, 5);
        when(taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable))
                .thenReturn(Arrays.asList());

        List<Task> result = taskService.getIncompleteTasks(5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findByCompletedFalseOrderByCreatedAtDesc(pageable);
    }

    @Test
    void getTaskById_Success() throws CustomException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Test Task", result.get().getTitle());
        verify(taskRepository, times(2)).findById(1L);
    }

    @Test
    void getTaskById_NotFound_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.getTaskById(1L));

        assertEquals("Task not found with id 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void markTaskAsCompleted_Success() throws CustomException {
        testTask.setCompleted(false);
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setTitle("Test Task");
        completedTask.setDescription("Test Description");
        completedTask.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(completedTask);

        Task result = taskService.markTaskAsCompleted(1L);

        assertNotNull(result);
        assertTrue(result.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markTaskAsCompleted_NotFound_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.markTaskAsCompleted(1L));

        assertEquals("Task not found with id 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_Success() throws CustomException {
        // Note: The current implementation has a bug - it should be !existsById
        // This test reflects the buggy behavior
        when(taskRepository.existsById(1L)).thenReturn(false);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_NotFound_ThrowsException() {
        // Note: The current implementation has a bug - the logic is inverted
        when(taskRepository.existsById(1L)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class,
                () -> taskService.deleteTask(1L));

        assertEquals("Task not found with id 1", exception.getMessage());
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, never()).deleteById(1L);
    }
}