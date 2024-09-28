package com.mateusz.todos.kotlin_sboot_todos_backend.controllers

import com.mateusz.todos.kotlin_sboot_todos_backend.dto.UserRegistrationRequest
import com.mateusz.todos.kotlin_sboot_todos_backend.dto.UserRegistrationResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserRegistrationController(private var userDetailsManager: JdbcUserDetailsManager) {
    @PostMapping(
        value = ["/signup"],
        consumes = ["application/json", "application/xml"],
        produces = ["application/json", "application/xml"]
    )
    fun registerUser(
        @RequestBody registrationRequest: @Valid UserRegistrationRequest
    ): ResponseEntity<UserRegistrationResponse> {
        if (userDetailsManager.userExists(registrationRequest.username)) {
            return ResponseEntity.badRequest().body(UserRegistrationResponse("Username is already taken!"))
        }

        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val user = User.withUsername(registrationRequest.username)
            .password(encoder.encode(registrationRequest.password))
            .authorities("ROLE_USER")
            .build()

        userDetailsManager.createUser(user)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(UserRegistrationResponse("User registered successfully!"))
    }
}