package com.uamishop.ventas.domain;

import com.uamishop.catalogo.domain.ProductoId;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CarritoTest {
    
    @Test
    void testAgregarProducto() {
        CarritoId cartId = new CarritoId("C1");
        ClienteId clientId = new ClienteId("CLI1");
        Carrito carrito = new Carrito(cartId, clientId);
        
        ProductoRef producto = new ProductoRef(
            new ProductoId("P1"), "Laptop", "LAP-001"
        );
        Money precio = new Money(new BigDecimal("100"), "MXN");
        
        carrito.agregarProducto(producto, 2, precio);
        
        assertEquals(1, carrito.getItems().size());
        assertEquals(EstadoCarrito.ACTIVO, carrito.getEstado());
    }
    
    @Test
    void testIniciarCheckout() {
        CarritoId cartId = new CarritoId("C1");
        ClienteId clientId = new ClienteId("CLI1");
        Carrito carrito = new Carrito(cartId, clientId);
        
        ProductoRef producto = new ProductoRef(
            new ProductoId("P1"), "Laptop", "LAP-001"
        );
        Money precio = new Money(new BigDecimal("100"), "MXN");
        
        carrito.agregarProducto(producto, 1, precio);
        carrito.iniciarCheckout();
        
        assertEquals(EstadoCarrito.EN_CHECKOUT, carrito.getEstado());
    }
}