package com.todoapp.todo_backend.repository;

import com.todoapp.todo_backend.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        task1 = new Task();
        task1.setTitle("First Task");
        task1.setDescription("First Description");
        task1.setCompleted(false);

        task2 = new Task();
        task2.setTitle("Second Task");
        task2.setDescription("Second Description");
        task2.setCompleted(false);

        task3 = new Task();
        task3.setTitle("Completed Task");
        task3.setDescription("Completed Description");
        task3.setCompleted(true);
    }

    @Test
    void saveTask_Success() {
        Task savedTask = taskRepository.save(task1);

        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());
        assertEquals("First Task", savedTask.getTitle());
        assertEquals("First Description", savedTask.getDescription());
        assertFalse(savedTask.getCompleted());
        assertNotNull(savedTask.getCreatedAt());
    }

    @Test
    void findById_Success() {
        Task savedTask = taskRepository.save(task1);

        Optional<Task> found = taskRepository.findById(savedTask.getId());

        assertTrue(found.isPresent());
        assertEquals(savedTask.getId(), found.get().getId());
        assertEquals("First Task", found.get().getTitle());
    }

    @Test
    void findById_NotFound() {
        Optional<Task> found = taskRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findByCompletedFalseOrderByCreatedAtDesc_ReturnsIncompleteTasks() throws InterruptedException {
        // Save tasks with slight delays to ensure different timestamps
        taskRepository.save(task1);
        Thread.sleep(10);
        taskRepository.save(task2);
        Thread.sleep(10);
        taskRepository.save(task3);

        Pageable pageable = PageRequest.of(0, 5);
        List<Task> incompleteTasks = taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable);

        assertNotNull(incompleteTasks);
        assertEquals(2, incompleteTasks.size());
        // Most recent incomplete task should be first
        assertEquals("Second Task", incompleteTasks.get(0).getTitle());
        assertEquals("First Task", incompleteTasks.get(1).getTitle());
    }

    @Test
    void findByCompletedFalseOrderByCreatedAtDesc_WithLimit() throws InterruptedException {
        // Create 3 incomplete tasks
        taskRepository.save(task1);
        Thread.sleep(10);
        taskRepository.save(task2);
        Thread.sleep(10);

        Task task4 = new Task();
        task4.setTitle("Third Task");
        task4.setDescription("Third Description");
        task4.setCompleted(false);
        taskRepository.save(task4);

        Pageable pageable = PageRequest.of(0, 2);
        List<Task> incompleteTasks = taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable);

        assertNotNull(incompleteTasks);
        assertEquals(2, incompleteTasks.size());
    }

    @Test
    void findByCompletedFalseOrderByCreatedAtDesc_NoIncompleteTasks() {
        taskRepository.save(task3); // Only completed task

        Pageable pageable = PageRequest.of(0, 5);
        List<Task> incompleteTasks = taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable);

        assertNotNull(incompleteTasks);
        assertTrue(incompleteTasks.isEmpty());
    }

    @Test
    void updateTask_Success() {
        Task savedTask = taskRepository.save(task1);
        savedTask.setTitle("Updated Title");
        savedTask.setCompleted(true);

        Task updatedTask = taskRepository.save(savedTask);

        assertEquals("Updated Title", updatedTask.getTitle());
        assertTrue(updatedTask.getCompleted());
    }

    @Test
    void deleteTask_Success() {
        Task savedTask = taskRepository.save(task1);
        Long taskId = savedTask.getId();

        taskRepository.deleteById(taskId);

        Optional<Task> deletedTask = taskRepository.findById(taskId);
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void existsById_ReturnsTrue_WhenTaskExists() {
        Task savedTask = taskRepository.save(task1);

        boolean exists = taskRepository.existsById(savedTask.getId());

        assertTrue(exists);
    }

    @Test
    void existsById_ReturnsFalse_WhenTaskDoesNotExist() {
        boolean exists = taskRepository.existsById(999L);

        assertFalse(exists);
    }

    @Test
    void findAll_ReturnsAllTasks() {
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        List<Task> allTasks = taskRepository.findAll();

        assertNotNull(allTasks);
        assertEquals(3, allTasks.size());
    }

    @Test
    void count_ReturnsCorrectCount() {
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        long count = taskRepository.count();

        assertEquals(3, count);
    }
}