// Archivo: /workspaces/Los-Animaniacs/uamishop-ordenes/src/main/java/com/uamishop/shared/exception/BusinessRuleException.java

package com.uamishop.shared.exception;

// Excepción para violaciones de reglas de negocio (HTTP 422) 
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}