package com.uamishop.shared.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    
    @Test
    void testSumar() {
        Money m1 = new Money(new BigDecimal("100"), "MXN");
        Money m2 = new Money(new BigDecimal("50"), "MXN");
        
        Money resultado = m1.sumar(m2);
        
        assertEquals(new BigDecimal("150"), resultado.getMonto());
    }
    
    @Test
    void testSumarMonedasDiferentes() {
        Money m1 = new Money(new BigDecimal("100"), "MXN");
        Money m2 = new Money(new BigDecimal("50"), "USD");
        
        assertThrows(IllegalArgumentException.class, () -> {
            m1.sumar(m2);
        });
    }
}