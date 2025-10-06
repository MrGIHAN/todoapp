package com.todoapp.todo_backend.repository;

import com.todoapp.todo_backend.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletedFalseOrderByCreatedAtDesc(Pageable pageable);

}
