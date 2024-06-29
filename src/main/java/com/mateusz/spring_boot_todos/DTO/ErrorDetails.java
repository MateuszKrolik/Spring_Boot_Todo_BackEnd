package com.mateusz.spring_boot_todos.DTO;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime timeStamp, String message, String details) {
}