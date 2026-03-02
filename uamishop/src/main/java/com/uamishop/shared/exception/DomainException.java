package com.uamishop.shared.exception;

/**
 * Excepción base para errores de dominio en cualquier módulo.
 * Requerida en el Shared Kernel según Práctica 5.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}