package com.mateusz.todos.kotlin_sboot_todos_backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class TodoNotFoundException(message: String?) : RuntimeException(message)