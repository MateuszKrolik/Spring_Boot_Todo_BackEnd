package com.mateusz.spring_boot_todos.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import com.mateusz.spring_boot_todos.DTO.ErrorDetails;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(Exception.class)
        public final ResponseEntity<ErrorDetails> handleAllException(Exception ex, WebRequest request)
                        throws Exception {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(TodoNotFoundException.class)
        public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
                        throws Exception {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public final ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex,
                        WebRequest request) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public final ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex,
                        WebRequest request) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                                request.getDescription(false));
                return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                        WebRequest request) {

                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                                "Total errors are: " + ex.getErrorCount() + ", First error is: "
                                                + ex.getFieldError().getDefaultMessage(),
                                request.getDescription(false));

                return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

        }

}
