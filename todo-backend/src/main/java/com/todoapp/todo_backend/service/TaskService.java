package com.todoapp.todo_backend.service;

import java.util.List;
import java.util.Optional;

import com.todoapp.todo_backend.dto.requestDto.TaskRequestDTO;
import com.todoapp.todo_backend.exception.CustomException;
import com.todoapp.todo_backend.model.Task;

public interface TaskService {

    Task createTask(TaskRequestDTO taskRequestDTO) throws CustomException;

    List<Task> getIncompleteTasks(int limit);

    Optional<Task>  getTaskById(Long id) throws CustomException;

    Task markTaskAsCompleted(Long id) throws CustomException;

    void deleteTask(Long id) throws CustomException;

}
