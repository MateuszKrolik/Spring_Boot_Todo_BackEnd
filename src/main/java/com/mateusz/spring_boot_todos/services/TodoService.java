package com.mateusz.spring_boot_todos.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.mateusz.spring_boot_todos.models.Todo;

import jakarta.validation.Valid;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<>();

    private static int todosCount = 0;
    static {// static variable initialization
        todos.add(new Todo(++todosCount, "mateusz", "learn aws", LocalDate.now().plusYears(1), false));

        todos.add(new Todo(++todosCount, "mateusz", "learn devOps", LocalDate.now().plusYears(2), false));

        todos.add(new Todo(++todosCount, "mateusz", "learn fullStack", LocalDate.now().plusYears(3), false));
    }

    public List<Todo> findByUsername(String username) {
        Predicate<? super Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(username);
        return todos.stream().filter(predicate).toList();
    }

    public Todo addTodo(String username, String description, LocalDate targetDate, boolean done) {
        Todo todo = new Todo(++todosCount, username, description, targetDate, done);
        todos.add(todo);
        return todo;
    }

    public void deleteById(int id) {
        Predicate<? super Todo> predicate = todo -> todo.getId() == id;
        todos.removeIf(predicate);
    }

    public Optional<Todo> findByIdAndUsername(int id, String username) {
        Predicate<? super Todo> predicate = todo -> todo.getId() == id && todo.getUsername().equalsIgnoreCase(username);
        return todos.stream().filter(predicate).findFirst();
    }

    public Todo updateTodo(@Valid Todo todo) {
        deleteById(todo.getId());
        todos.add(todo);
        return todo;
    }
}