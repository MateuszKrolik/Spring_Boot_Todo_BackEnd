package com.mateusz.todos.kotlin_sboot_todos_backend.repositories

import com.mateusz.todos.kotlin_sboot_todos_backend.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User?, String?> {
    fun findByUsername(username: String?): Optional<User?>?
}