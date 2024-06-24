package com.mateusz.spring_boot_todos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mateusz.spring_boot_todos.exception.TodoNotFoundException;
import com.mateusz.spring_boot_todos.models.Todo;
import com.mateusz.spring_boot_todos.services.TodoService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1")
public class TodoResource {

    private TodoService todoService;

    // constructor dependency injection
    public TodoResource(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/users/{username}/todos")
    public List<Todo> retrieveAllTodos(@PathVariable String username) {
        return todoService.findByUsername(username);
    }

    @GetMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> retrieveOneTodo(@PathVariable String username, @PathVariable int id) {
        Optional<Todo> todo = todoService.findByIdAndUsername(id, username);

        if (todo.isEmpty()) {
            throw new TodoNotFoundException("couldn't find todo with id: " + id + " for username: " + username);
        }
        return ResponseEntity.of(todo);
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteOneTodo(@PathVariable String username, @PathVariable int id) {
        Optional<Todo> todo = todoService.findByIdAndUsername(id, username);

        if (todo.isEmpty()) {
            throw new TodoNotFoundException("couldn't delete todo with id: " + id + " for username: " + username);
        }
        todoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
