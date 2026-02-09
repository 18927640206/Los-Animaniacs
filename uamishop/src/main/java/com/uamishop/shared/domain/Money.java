package com.uamishop.shared.domain;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Money {
	 private final BigDecimal monto;
	    private final String moneda;

	    public Money(BigDecimal monto, String moneda) {
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
}
