package com.todoapp.todo_backend.service.impl;

import com.todoapp.todo_backend.exception.CustomException;
import com.todoapp.todo_backend.model.Task;
import com.todoapp.todo_backend.dto.requestDto.TaskRequestDTO;
import com.todoapp.todo_backend.repository.TaskRepository;
import com.todoapp.todo_backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    public TaskServiceImp(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(TaskRequestDTO taskRequestDTO) throws CustomException {
        if (taskRequestDTO.getTitle() == null || taskRequestDTO.getTitle().trim().isEmpty()) {
            throw new CustomException("Task title cannot be empty");
        }

        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setCompleted(false);

        return taskRepository.save(task);
    }

    @Override
    public List<Task> getIncompleteTasks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Optional<Task> getTaskById(Long id) throws CustomException {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new CustomException("Task not found with id " + id)
        );
        return taskRepository.findById(id);
    }



    @Override
    public Task markTaskAsCompleted(Long id) throws CustomException {
        Task task = taskRepository.findById(id).orElseThrow(
                ()-> new CustomException("Task not found with id " + id)
        );
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) throws CustomException {
        if (taskRepository.existsById(id)) {
            throw new CustomException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }
}
