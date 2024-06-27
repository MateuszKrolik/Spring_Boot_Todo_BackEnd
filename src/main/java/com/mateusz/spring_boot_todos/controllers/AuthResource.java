package com.mateusz.spring_boot_todos.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.mateusz.spring_boot_todos.models.AuthResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/v1")
public class AuthResource {

    @GetMapping("/basicauth")
    public AuthResponse basicAuthCheck() {
        return new AuthResponse("Success");
    }

}