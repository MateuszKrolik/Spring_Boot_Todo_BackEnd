package com.mateusz.spring_boot_todos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mateusz.spring_boot_todos.models.Todo;
import java.util.List;


public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUser_Username(String username);
}