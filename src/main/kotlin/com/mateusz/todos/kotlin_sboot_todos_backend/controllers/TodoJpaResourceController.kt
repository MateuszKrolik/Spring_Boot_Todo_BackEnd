package com.mateusz.todos.kotlin_sboot_todos_backend.controllers

import com.mateusz.todos.kotlin_sboot_todos_backend.exception.TodoNotFoundException
import com.mateusz.todos.kotlin_sboot_todos_backend.models.Todo
import com.mateusz.todos.kotlin_sboot_todos_backend.models.User
import com.mateusz.todos.kotlin_sboot_todos_backend.repositories.TodoRepository
import com.mateusz.todos.kotlin_sboot_todos_backend.repositories.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/v1"])
@EnableMethodSecurity(prePostEnabled = true)
@PreAuthorize("hasAuthority('SCOPE_ROLE_USER') and #username == authentication.name")
class TodoJpaResourceController(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) {
    @GetMapping(
        value = ["/users/{username}/todos"], produces = ["application/json", "application/xml"]
    )
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun retrieveAllTodos(@PathVariable username: String?): List<Todo?>? {
        return todoRepository.findByUser_Username(username)
    }

    @GetMapping(
        value = ["/users/{username}/todos/{id}"], produces = ["application/json", "application/xml"]
    )
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun retrieveOneTodo(@PathVariable username: String, @PathVariable id: Int): ResponseEntity<Todo> {
        val todo = todoRepository.findById(id)
            .filter { t -> t?.user?.username.equals(username) } // Adjusted to navigate through User
            .orElseThrow { TodoNotFoundException("Todo not found for id: $id and username: $username") }
        return ResponseEntity.ok(todo)
    }

    @DeleteMapping(
        value = ["/users/{username}/todos/{id}"], produces = ["application/json", "application/xml"]
    )
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun deleteOneTodo(@PathVariable username: String, @PathVariable id: Int): ResponseEntity<Void> {
        val todo: Todo? = todoRepository.findById(id)
            .filter { t: Todo? -> t?.user?.username.equals(username) } // Adjusted to navigate through User
            .orElseThrow { TodoNotFoundException("Todo not found for id: $id and username: $username") }
        if (todo != null) {
            todoRepository.delete(todo)
        }
        return ResponseEntity.noContent().build()
    }

    @PutMapping(
        value = ["/users/{username}/todos/{id}"],
        consumes = ["application/json", "application/xml"],
        produces = ["application/json", "application/xml"]
    )
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun updateOneTodo(
        @PathVariable username: String, @PathVariable id: Int,
        @RequestBody todo: @Valid Todo?
    ): ResponseEntity<Todo> {
        val existingTodo: Todo? = todoRepository.findById(id)
            .filter { t: Todo? -> t?.user?.username.equals(username) } // Adjusted to navigate through User
            .orElseThrow { TodoNotFoundException("Todo not found for id: $id and username: $username") }
        if (existingTodo != null) {
            existingTodo.description = todo?.description
        }
        if (existingTodo != null) {
            existingTodo.targetDate = todo?.targetDate
        }
        if (existingTodo != null) {
            existingTodo.done = todo!!.done
        }
        val updatedTodo = existingTodo?.let { todoRepository.save(it) }
        return ResponseEntity.ok(updatedTodo)
    }

    @PostMapping(
        value = ["/users/{username}/todos"],
        consumes = ["application/json", "application/xml"],
        produces = ["application/json", "application/xml"]
    )
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun createOneTodo(
        @PathVariable username: String?,
        @RequestBody todo: @Valid Todo
    ): ResponseEntity<*> {
        val userOptional: Optional<User?>? = userRepository.findByUsername(username)
        if (!userOptional!!.isPresent) {
            return ResponseEntity.notFound().build<Any>()
        }
        val user = userOptional.get()
        todo.user = user
        val savedTodo = todoRepository.save(todo)
        return ResponseEntity.ok(savedTodo)
    }
}