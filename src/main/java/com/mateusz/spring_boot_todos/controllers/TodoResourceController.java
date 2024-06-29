package com.mateusz.spring_boot_todos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mateusz.spring_boot_todos.exception.TodoNotFoundException;
import com.mateusz.spring_boot_todos.models.Todo;
import com.mateusz.spring_boot_todos.services.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/v1")
@EnableMethodSecurity(prePostEnabled = true)
@PreAuthorize("hasAuthority('SCOPE_ROLE_USER') and #username == authentication.name")
public class TodoResourceController {

    private TodoService todoService;

    // constructor dependency injection
    public TodoResourceController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/users/{username}/todos")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public List<Todo> retrieveAllTodos(@PathVariable String username) {
        return todoService.findByUsername(username);
    }

    @GetMapping("/users/{username}/todos/{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<Todo> retrieveOneTodo(@PathVariable String username, @PathVariable int id) {
        Optional<Todo> todo = todoService.findByIdAndUsername(id, username);

        if (todo.isEmpty()) {
            throw new TodoNotFoundException("couldn't find todo with id: " + id + " for username: " + username);
        }
        return ResponseEntity.of(todo);
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<Void> deleteOneTodo(@PathVariable String username, @PathVariable int id) {
        Optional<Todo> todo = todoService.findByIdAndUsername(id, username);

        if (todo.isEmpty()) {
            throw new TodoNotFoundException("couldn't delete todo with id: " + id + " for username: " + username);
        }
        todoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{username}/todos/{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public Todo updateOneTodo(@PathVariable String username, @PathVariable int id,
            @Valid @RequestBody Todo todo) {

        todo.setUsername(username);
        todo.setId(id);

        Todo updatedTodo = todoService.updateTodo(todo);

        return updatedTodo;
    }

    @PostMapping("/users/{username}/todos")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public Todo createOneTodo(@PathVariable String username,
            @Valid @RequestBody Todo todo) {
        Todo createdTodo = todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), todo.isDone());

        return createdTodo;
    }

}
