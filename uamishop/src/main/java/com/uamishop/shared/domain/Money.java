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

}
