package com.uamishop.ordenes.domain;

import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.shared.domain.ProductoId;
//import com.uamishop.catalogo.domain.ProductoId;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class OrdenTest {
    
    @Test
    void testCrearOrden() {
        OrdenId ordenId = new OrdenId("ORD1");
        ClienteId clienteId = new ClienteId("CLI1");
        
        ItemOrden item = new ItemOrden(
            new ItemOrdenId("I1"),
            new ProductoRef(new ProductoId("P1"), "Laptop", "LAP-001"),
            2,
            new Money(new BigDecimal("100"), "MXN")
        );
        
        DireccionEnvio direccion = new DireccionEnvio(
            "Calle", "Colonia", "Ciudad", "Estado", "12345", "México", "5551234567"
        );
        
        Orden orden = new Orden(ordenId, clienteId, Arrays.asList(item), direccion);
        
        assertEquals(EstadoOrden.PENDIENTE, orden.getEstado());
        assertEquals(1, orden.getItems().size());
    }
    
    @Test
    void testConfirmarOrden() {
        OrdenId ordenId = new OrdenId("ORD1");
        ClienteId clienteId = new ClienteId("CLI1");
        
        ItemOrden item = new ItemOrden(
            new ItemOrdenId("I1"),
            new ProductoRef(new ProductoId("P1"), "Laptop", "LAP-001"),
            1,
            new Money(new BigDecimal("100"), "MXN")
        );
        
        DireccionEnvio direccion = new DireccionEnvio(
            "Calle", "Colonia", "Ciudad", "Estado", "12345", "México", "5551234567"
        );
        
        Orden orden = new Orden(ordenId, clienteId, Arrays.asList(item), direccion);
        orden.confirmar();
        
        assertEquals(EstadoOrden.CONFIRMADA, orden.getEstado());
    }
}