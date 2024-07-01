package com.mateusz.spring_boot_todos.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.mateusz.spring_boot_todos.models.Todo;
import com.mateusz.spring_boot_todos.models.User;
import com.mateusz.spring_boot_todos.repositories.TodoRepository;
import com.mateusz.spring_boot_todos.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoJpaResourceController.class)
public class TodoJpaResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "testUser", authorities = { "SCOPE_ROLE_USER" })
    public void retrieveAllTodos_forUser_ShouldReturnTodos() throws Exception {
        // Given
        String username = "testUser";
        Integer todoId = 1;
        User user = new User(username, "password", true, null);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        List<Todo> mockTodos = Arrays.asList(new Todo(todoId, user, "Complete unit tests", targetDate, false));
        given(todoRepository.findByUser_Username(username)).willReturn(mockTodos);

        // When & Then
        mockMvc.perform(get("/v1/users/{username}/todos", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(mockTodos.get(0).getId()))
                .andExpect(jsonPath("$[0].description").value("Complete unit tests"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[0].targetDate").value(targetDate.toString()));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = { "SCOPE_ROLE_USER" })
    public void retrieveOneTodo_forUser_ShouldReturnTodo() throws Exception {
        // Given
        String username = "testUser";
        Integer todoId = 1;
        User user = new User(username, "password", true, null);
        LocalDate targetDate = LocalDate.now().plusDays(1);
        Todo mockTodo = new Todo(todoId, user, "Complete unit tests", targetDate, false);
        given(todoRepository.findById(todoId)).willReturn(java.util.Optional.of(mockTodo));

        // When & Then
        mockMvc.perform(get("/v1/users/{username}/todos/{id}", username, todoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockTodo.getId()))
                .andExpect(jsonPath("$.description").value("Complete unit tests"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.targetDate").value(targetDate.toString()));
    }

}