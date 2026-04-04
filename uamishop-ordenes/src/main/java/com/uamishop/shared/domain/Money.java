// uamishop-ordenes/src/main/java/com/uamishop/shared/domain/Money.java
package com.uamishop.shared.domain;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Money {
    private BigDecimal monto;
    private String moneda;

    protected Money() {}

    public Money(BigDecimal monto, String moneda) {
        if (monto == null) throw new IllegalArgumentException("Monto no puede ser nulo");
        if (monto.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Monto no puede ser negativo");
        this.monto = monto;
        this.moneda = moneda;
    }

    public BigDecimal getMonto() { return monto; }
    public String getMoneda() { return moneda; }
    
    public Money sumar(Money otro) {
        if (otro == null) throw new IllegalArgumentException("Money a sumar no puede ser null");
        if (!this.moneda.equals(otro.moneda)) throw new IllegalArgumentException("No se pueden sumar montos con distinta moneda"); // RN-VO-01
        return new Money(this.monto.add(otro.monto), this.moneda);
    }

    public Money restar(Money otro) {
        if (otro == null) throw new IllegalArgumentException("Money a restar no puede ser null");
        if (!this.moneda.equals(otro.getMoneda())) throw new IllegalArgumentException("No se pueden restar montos con distinta moneda");
        BigDecimal resultado = this.monto.subtract(otro.getMonto());
        if (resultado.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("El resultado de una resta no puede ser negativo"); // RN-VO-02
        return new Money(resultado, this.moneda);
    }

    // Método faltante según el diagrama de la Práctica 2
    public Money multiplicar(int factor) {
        return new Money(this.monto.multiply(new BigDecimal(factor)), this.moneda);
    }
}