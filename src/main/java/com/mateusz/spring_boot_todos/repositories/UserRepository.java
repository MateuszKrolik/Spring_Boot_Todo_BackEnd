package com.mateusz.spring_boot_todos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mateusz.spring_boot_todos.models.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

}
