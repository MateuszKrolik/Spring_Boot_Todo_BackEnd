package com.mateusz.spring_boot_todos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mateusz.spring_boot_todos.DTO.JwtTokenRequest;
import com.mateusz.spring_boot_todos.DTO.JwtTokenResponse;
import com.mateusz.spring_boot_todos.services.JwtTokenService;

@RestController
public class JwtAuthenticationController {

    private final JwtTokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationController(JwtTokenService tokenService,
            AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> createToken(
            @RequestBody JwtTokenRequest jwtTokenRequest) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtTokenRequest.username(),
                jwtTokenRequest.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        var token = tokenService.createToken(authentication);

        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
