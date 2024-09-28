package com.mateusz.todos.kotlin_sboot_todos_backend.repositories

import com.mateusz.todos.kotlin_sboot_todos_backend.models.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo?, Int?> {
    @Suppress("FunctionName") // underscore for postgres
    fun findByUser_Username(username: String?): List<Todo?>?
}