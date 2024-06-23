package com.mateusz.spring_boot_todos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringBootTodosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTodosApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(@Value("${ALLOWED_ORIGIN:http://localhost:3000}") String allowedOrigin) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*").allowedOrigins(allowedOrigin);
			}
		};
	}
}
