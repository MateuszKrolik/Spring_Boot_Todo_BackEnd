package com.mateusz.todos.kotlin_sboot_todos_backend

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class KotlinSbootTodosBackendApplication {
    @Bean
    fun corsConfigurer(@Value("\${ALLOWED_ORIGIN:http://localhost:3000}") allowedOrigin: String?): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry
                    .addMapping("/**")
                    .allowedMethods("*")
                    .allowedOrigins(allowedOrigin)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinSbootTodosBackendApplication>(*args)
}

