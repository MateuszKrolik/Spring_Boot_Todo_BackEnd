package com.mateusz.spring_boot_todos.security;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1.Enable access to preflight request
        // 2.basic auth
        return http
                // Lambda DSL w/ "authz" meaning authorization
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // enable access to root url for all users for possible Cloud HealthCheck
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated() // 1. All requests should be authenticated
                )
                .httpBasic(withDefaults()) // 2. If a request is not authenticated, a web page is shown
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 3.make
                                                                                                              // session
                                                                                                              // stateless
                                                                                                              // (best
                                                                                                              // practice
                                                                                                              // when
                                                                                                              // disabling
                                                                                                              // CSRF)
                .csrf(csrf -> csrf
                        .disable() // 4. Disable CSRF (impacts POST & PUT routes)
                )
                .build();

    }
}