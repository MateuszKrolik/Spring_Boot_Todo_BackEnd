package com.mateusz.todos.kotlin_sboot_todos_backend.dto

import java.time.LocalDateTime

data class ErrorDetails(val timeStamp:LocalDateTime, val message:String, val details: String)