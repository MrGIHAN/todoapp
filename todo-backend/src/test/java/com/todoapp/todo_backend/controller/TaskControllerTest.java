package com.todoapp.todo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.todo_backend.dto.requestDto.TaskRequestDTO;
import com.todoapp.todo_backend.exception.CustomException;
import com.todoapp.todo_backend.model.Task;
import com.todoapp.todo_backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

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
    void createTask_Success() throws Exception {
        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService, times(1)).createTask(any(TaskRequestDTO.class));
    }

    @Test
    void createTask_InvalidData_ReturnsInternalServerError() throws Exception {
        when(taskService.createTask(any(TaskRequestDTO.class)))
                .thenThrow(new CustomException("Task title cannot be empty"));

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isInternalServerError());

        verify(taskService, times(1)).createTask(any(TaskRequestDTO.class));
    }

    @Test
    void getAllTask_Success() throws Exception {
        Task task1 = new Task(1L, "Task 1", "Description 1", false, LocalDateTime.now());
        Task task2 = new Task(2L, "Task 2", "Description 2", false, LocalDateTime.now());
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getIncompleteTasks(5)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/gettask")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).getIncompleteTasks(5);
    }

    @Test
    void getAllTask_EmptyList() throws Exception {
        when(taskService.getIncompleteTasks(5)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/tasks/gettask")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(taskService, times(1)).getIncompleteTasks(5);
    }

    @Test
    void getTaskById_Success() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(testTask));

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_ThrowsException() throws Exception {
        when(taskService.getTaskById(1L))
                .thenThrow(new CustomException("Task not found with id 1"));

        mockMvc.perform(get("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void markTaskAsCompleted_Success() throws Exception {
        testTask.setCompleted(true);
        when(taskService.markTaskAsCompleted(1L)).thenReturn(testTask);

        mockMvc.perform(put("/api/tasks/1/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService, times(1)).markTaskAsCompleted(1L);
    }

    @Test
    void markTaskAsCompleted_NotFound() throws Exception {
        when(taskService.markTaskAsCompleted(1L))
                .thenThrow(new CustomException("Task not found with id 1"));

        mockMvc.perform(put("/api/tasks/1/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).markTaskAsCompleted(1L);
    }

    @Test
    void deleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        doThrow(new CustomException("Task not found with id 1"))
                .when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(1L);
    }
}