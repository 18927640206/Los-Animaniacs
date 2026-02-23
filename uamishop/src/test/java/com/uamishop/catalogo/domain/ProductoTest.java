package com.uamishop.catalogo.domain;

import com.uamishop.shared.domain.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {
    
    @Test
    void testCrearProducto() {
        ProductoId id = new ProductoId("P1");
        Money precio = new Money(new BigDecimal("100"), "MXN");
        CategoriaId catId = new CategoriaId("CAT1");
        
        // Ajustado al constructor que definimos en la práctica anterior
        Producto p = new Producto(id, "Laptop", "Desc", precio, catId);
        
        assertEquals("Laptop", p.getNombre());
        assertEquals(0, p.getImagenes().size());
    }
    
    @Test
    void testCambiarPrecio() {
        ProductoId id = new ProductoId("P1");
        Money precio = new Money(new BigDecimal("100"), "MXN");
        CategoriaId catId = new CategoriaId("CAT1");
        Producto p = new Producto(id, "Laptop", "Desc", precio, catId);
        
        Money nuevoPrecio = new Money(new BigDecimal("150"), "MXN");
        p.cambiarPrecio(nuevoPrecio);
        
        assertEquals(nuevoPrecio.getMonto(), p.getPrecio().getMonto());
    }
}