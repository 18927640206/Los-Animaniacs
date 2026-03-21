package com.uamishop.catalogo.shared.exception;

// Excepción para recursos no encontrados (HTTP 404) 
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}