package com.mateusz.todos.kotlin_sboot_todos_backend.controllers

import com.mateusz.todos.kotlin_sboot_todos_backend.models.Todo
import com.mateusz.todos.kotlin_sboot_todos_backend.models.User
import com.mateusz.todos.kotlin_sboot_todos_backend.repositories.TodoRepository
import com.mateusz.todos.kotlin_sboot_todos_backend.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.util.*

@WebMvcTest(TodoJpaResourceController::class)
@Suppress("UNCHECKED_CAST")
class TodoJpaResourceControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var todoRepository: TodoRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    @WithMockUser(username = "testUser", authorities = ["SCOPE_ROLE_USER"])
    @Throws(Exception::class)
    fun retrieveAllTodos_forUser_ShouldReturnTodos() {
        // Given
        val username = "testUser"
        val todoId = 1
        val user = User(username, "password", true, null)
        val targetDate = LocalDate.now().plusDays(1)
        val mockTodos = listOf(Todo(todoId, user, "Complete unit tests", targetDate, false))
        BDDMockito.given(
            todoRepository.findByUser_Username(username)
        ).willReturn(mockTodos)

        // When & Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/users/{username}/todos", username)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockTodos.first().id))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Complete unit tests"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].done").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetDate").value(targetDate.toString()))
    }

    @Test
    @WithMockUser(username = "testUser", authorities = ["SCOPE_ROLE_USER"])
    @Throws(java.lang.Exception::class)
    fun retrieveOneTodo_forUser_ShouldReturnTodo() {
        // Given
        val username = "testUser"
        val todoId = 1
        val user = User(username, "password", true, null)
        val targetDate = LocalDate.now().plusDays(1)
        val mockTodo= Todo(todoId, user, "Complete unit tests", targetDate, false)
        BDDMockito.given(todoRepository.findById(todoId)).willReturn(Optional.of(mockTodo) as Optional<Todo?>)

        // When & Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/users/{username}/todos/{id}", username, todoId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockTodo.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Complete unit tests"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.targetDate").value(targetDate.toString()))
    }
}