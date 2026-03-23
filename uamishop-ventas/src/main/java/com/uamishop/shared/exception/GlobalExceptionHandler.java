// Archivo: /workspaces/Los-Animaniacs/uamishop-ventas/src/main/java/com/uamishop/shared/exception/GlobalExceptionHandler.java

package com.uamishop.shared.exception;

import com.uamishop.shared.api.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class); 

    // Maneja HTTP 404 - Recurso no encontrado 
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage()); 
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Not Found", 
            ex.getMessage(),
            getPath(request)
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Maneja HTTP 422 - Reglas de negocio (Tu DomainException) 
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex, WebRequest request) {
        log.warn("Regla de negocio violada: {}", ex.getMessage()); 
        ApiError apiError = new ApiError(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Unprocessable Entity", 
            ex.getMessage(),
            getPath(request)
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // Maneja HTTP 400 - Bad Request (Errores de validación @Valid) 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request", // Ajustado para que tus tests pasen 
            errorMessage,
            getPath(request)
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Maneja HTTP 500 - Errores generales inesperados 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Error interno del servidor: ", ex);
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocurrió un error inesperado en el sistema",
            getPath(request)
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getRequest().getRequestURI(); 
        }
        return "";
    }
}