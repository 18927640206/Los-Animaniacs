package com.uamishop.shared.domain;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Money {
    // 1. Quitamos el 'final' para que el constructor de JPA pueda funcionar
    private BigDecimal monto;
    private String moneda;

    // 2. Constructor vacío para JPA (ahora sí compila)
    protected Money() {}

    public Money(BigDecimal monto, String moneda) {
        if (monto == null) {
            throw new IllegalArgumentException("Monto no puede ser nulo");
        }
        if (monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monto no puede ser negativo");
        }
        this.monto = monto;
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getMoneda() {
        return moneda;
    }
    
    public Money sumar(Money otro) {
        if (otro == null) {
            throw new IllegalArgumentException("Money a sumar no puede ser null");
        }
        if (!this.moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException("No se pueden sumar montos con distinta moneda");
        }
        return new Money(this.monto.add(otro.monto), this.moneda);
    }

    public Money restar(Money otro) {
        if (otro == null) {
            throw new IllegalArgumentException("Money a restar no puede ser null");
        }
        if (!this.moneda.equals(otro.getMoneda())) {
            throw new IllegalArgumentException("No se pueden restar montos con distinta moneda");
        }
        BigDecimal resultado = this.monto.subtract(otro.getMonto());
        if (resultado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El resultado de una resta no puede ser negativo");
        }
        return new Money(resultado, this.moneda);
    }
}