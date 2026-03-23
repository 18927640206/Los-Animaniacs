package com.uamishop.ventas.domain;
import com.uamishop.shared.domain.Money;
import java.math.BigDecimal;

public class DescuentoAplicado {
    private final String codigo;
    private final BigDecimal porcentaje;
    private final Money montoDescuento;

    public DescuentoAplicado(String codigo, BigDecimal porcentaje, Money subtotal) {
        // RN-VEN-16
        if (porcentaje.compareTo(new BigDecimal("30")) > 0) {
            throw new IllegalArgumentException("Máximo 30% de descuento");
        }
        
        this.codigo = codigo;
        this.porcentaje = porcentaje;
        
        // Calcular monto
        BigDecimal descuento = subtotal.getMonto()
            .multiply(porcentaje)
            .divide(new BigDecimal("100"));
        this.montoDescuento = new Money(descuento, subtotal.getMoneda());
    }
    
    public String getCodigo() { return codigo; }
    public Money getMontoDescuento() { return montoDescuento; }
}
