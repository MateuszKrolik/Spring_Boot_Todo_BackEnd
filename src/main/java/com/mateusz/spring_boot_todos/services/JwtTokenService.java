package com.mateusz.spring_boot_todos.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 30))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(authority->authority.getAuthority())
                .collect(Collectors.joining(" "));
    }
}
