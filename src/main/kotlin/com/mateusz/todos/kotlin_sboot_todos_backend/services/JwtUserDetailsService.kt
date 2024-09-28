package com.mateusz.todos.kotlin_sboot_todos_backend.services

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.stream.Collectors

@Service
class JwtTokenService(private val jwtEncoder: JwtEncoder) {
    fun createToken(authentication: Authentication): String {
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds((60 * 30).toLong()))
            .subject(authentication.name)
            .claim("scope", createScope(authentication))
            .build()

        return jwtEncoder
            .encode(JwtEncoderParameters.from(claims))
            .tokenValue
    }

    private fun createScope(authentication: Authentication): String {
        return authentication
            .authorities
            .stream()
            .map { authority: GrantedAuthority? -> authority!!.authority }
            .collect(Collectors.joining(" "))
    }
}