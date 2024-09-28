package com.mateusz.todos.kotlin_sboot_todos_backend.services

import com.mateusz.todos.kotlin_sboot_todos_backend.models.SecurityUser
import com.mateusz.todos.kotlin_sboot_todos_backend.repositories.UserRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailsService(@param:Lazy private val userRepository: UserRepository) :
    UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository
            .findByUsername(username)
            ?.map { SecurityUser(it!!) }
            ?.orElseThrow { UsernameNotFoundException("Username not found: $username") } ?: throw UsernameNotFoundException("Username not found: $username")
    }
}
