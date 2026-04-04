// uamishop-ordenes/src/main/java/com/uamishop/shared/exception/ResourceNotFoundException.java

package com.uamishop.shared.exception;

// Excepción para recursos no encontrados (HTTP 404) 
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}