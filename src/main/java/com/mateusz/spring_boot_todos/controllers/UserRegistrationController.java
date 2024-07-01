package com.mateusz.spring_boot_todos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mateusz.spring_boot_todos.DTO.UserRegistrationRequest;
import com.mateusz.spring_boot_todos.DTO.UserRegistrationResponse;

import jakarta.validation.Valid;

import org.springframework.security.core.userdetails.User;

@RestController
@RequestMapping("/v1")
public class UserRegistrationController {

    private final JdbcUserDetailsManager userDetailsManager;

    public UserRegistrationController(JdbcUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping(value = "/signup", consumes = { "application/json", "application/xml" }, produces = {
            "application/json",
            "application/xml" })
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest registrationRequest) {
        if (userDetailsManager.userExists(registrationRequest.username())) {
            return ResponseEntity.badRequest().body(new UserRegistrationResponse("Username is already taken!"));
        }

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var user = User.withUsername(registrationRequest.username())
                .password(encoder.encode(registrationRequest.password()))
                .authorities("ROLE_USER")
                .build();

        userDetailsManager.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserRegistrationResponse("User registered successfully!"));

    }
}