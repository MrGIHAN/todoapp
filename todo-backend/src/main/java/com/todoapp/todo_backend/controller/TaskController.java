package com.todoapp.todo_backend.controller;

import com.todoapp.todo_backend.dto.requestDto.TaskRequestDTO;
import com.todoapp.todo_backend.exception.CustomException;
import com.todoapp.todo_backend.model.Task;

import com.todoapp.todo_backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public  ResponseEntity<Task> createTask(@RequestBody TaskRequestDTO taskRequestDTO){
        try {
            Task createdTask = taskService.createTask(taskRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        }catch (CustomException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gettask")
    public ResponseEntity<List<Task>> getAllTask(){
        List<Task> tasks = taskService.getIncompleteTasks(5);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) throws CustomException {
        return taskService.getTaskById(id)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        try {
            Task completedTask = taskService.markTaskAsCompleted(id);
            return new ResponseEntity<>(completedTask, HttpStatus.OK);
        } catch ( CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
