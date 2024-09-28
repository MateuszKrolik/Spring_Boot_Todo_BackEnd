package com.mateusz.todos.kotlin_sboot_todos_backend.controllers

import com.mateusz.todos.kotlin_sboot_todos_backend.dto.JwtTokenRequest
import com.mateusz.todos.kotlin_sboot_todos_backend.dto.JwtTokenResponse
import com.mateusz.todos.kotlin_sboot_todos_backend.services.JwtTokenService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class JwtAuthenticationController(
    private val tokenService: JwtTokenService,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping(
        value = ["/login"],
        consumes = ["application/json", "application/xml"],
        produces = ["application/json", "application/xml"]
    )
    fun createToken(
        @RequestBody jwtTokenRequest: JwtTokenRequest
    ): ResponseEntity<JwtTokenResponse> {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            jwtTokenRequest.username,
            jwtTokenRequest.password
        )

        val authentication = authenticationManager.authenticate(authenticationToken)

        val token: String = tokenService.createToken(authentication)

        return ResponseEntity.ok(JwtTokenResponse(token))
    }
}