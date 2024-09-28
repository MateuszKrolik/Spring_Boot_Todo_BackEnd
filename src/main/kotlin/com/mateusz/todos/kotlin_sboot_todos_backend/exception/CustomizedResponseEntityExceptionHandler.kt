package com.mateusz.todos.kotlin_sboot_todos_backend.exception

import com.mateusz.todos.kotlin_sboot_todos_backend.dto.ErrorDetails
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    @Throws(Exception::class)
    fun handleAllException(ex: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ex.message?.let {
            ErrorDetails(
                LocalDateTime.now(), it,
                request.getDescription(false)
            )
        }
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(TodoNotFoundException::class)
    @Throws(Exception::class)
    fun handleUserNotFoundException(ex: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ex.message?.let {
            ErrorDetails(
                LocalDateTime.now(), it,
                request.getDescription(false)
            )
        }
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<ErrorDetails> {
        val errorDetails = ex.message?.let {
            ErrorDetails(
                LocalDateTime.now(), it,
                request.getDescription(false)
            )
        }
        return ResponseEntity(errorDetails, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(
        ex: BadCredentialsException,
        request: WebRequest
    ): ResponseEntity<ErrorDetails> {
        val errorDetails = ex.message?.let {
            ErrorDetails(
                LocalDateTime.now(), it,
                request.getDescription(false)
            )
        }
        return ResponseEntity(errorDetails, HttpStatus.UNAUTHORIZED)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(),
            ("Total errors are: " + ex.errorCount + ", First error is: "
                    + ex.fieldError!!.defaultMessage),
            request.getDescription(false)
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }
}