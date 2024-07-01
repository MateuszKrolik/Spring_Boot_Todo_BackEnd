package com.mateusz.spring_boot_todos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mateusz.spring_boot_todos.exception.TodoNotFoundException;
import com.mateusz.spring_boot_todos.models.Todo;
import com.mateusz.spring_boot_todos.models.User;
import com.mateusz.spring_boot_todos.repositories.TodoRepository;
import com.mateusz.spring_boot_todos.repositories.UserRepository;

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
@RequestMapping(value = "/v1")
@EnableMethodSecurity(prePostEnabled = true)
@PreAuthorize("hasAuthority('SCOPE_ROLE_USER') and #username == authentication.name")
public class TodoJpaResourceController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoJpaResourceController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/users/{username}/todos", produces = { "application/json",
            "application/xml" })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public List<Todo> retrieveAllTodos(@PathVariable String username) {
        return todoRepository.findByUser_Username(username);
    }

    @GetMapping(value = "/users/{username}/todos/{id}", produces = { "application/json",
            "application/xml" })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<Todo> retrieveOneTodo(@PathVariable String username, @PathVariable Integer id) {
        Todo todo = todoRepository.findById(id)
                .filter(t -> t.getUser().getUsername().equals(username)) // Adjusted to navigate through User
                .orElseThrow(
                        () -> new TodoNotFoundException("Todo not found for id: " + id + " and username: " + username));
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping(value = "/users/{username}/todos/{id}", produces = { "application/json",
            "application/xml" })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<Void> deleteOneTodo(@PathVariable String username, @PathVariable Integer id) {
        Todo todo = todoRepository.findById(id)
                .filter(t -> t.getUser().getUsername().equals(username)) // Adjusted to navigate through User
                .orElseThrow(
                        () -> new TodoNotFoundException("Todo not found for id: " + id + " and username: " + username));
        todoRepository.delete(todo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/users/{username}/todos/{id}", consumes = { "application/json",
            "application/xml" }, produces = { "application/json",
                    "application/xml" })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<Todo> updateOneTodo(@PathVariable String username, @PathVariable Integer id,
            @Valid @RequestBody Todo todo) {
        Todo existingTodo = todoRepository.findById(id)
                .filter(t -> t.getUser().getUsername().equals(username)) // Adjusted to navigate through User
                .orElseThrow(
                        () -> new TodoNotFoundException("Todo not found for id: " + id + " and username: " + username));
        existingTodo.setDescription(todo.getDescription());
        existingTodo.setTargetDate(todo.getTargetDate());
        existingTodo.setDone(todo.isDone());
        Todo updatedTodo = todoRepository.save(existingTodo);
        return ResponseEntity.ok(updatedTodo);
    }

    @PostMapping(value = "/users/{username}/todos", consumes = { "application/json", "application/xml" }, produces = {
            "application/json",
            "application/xml" })
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<?> createOneTodo(@PathVariable String username,
            @Valid @RequestBody Todo todo) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);
        return ResponseEntity.ok(savedTodo);
    }

}