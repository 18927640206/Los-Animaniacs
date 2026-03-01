package com.uamishop.ventas;

import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;
import com.uamishop.ventas.api.CarritoResumen;
import com.uamishop.ventas.api.ItemCarritoResumen;
import com.uamishop.ventas.api.VentasApi;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.domain.CarritoId;
import com.uamishop.ventas.domain.ItemCarrito;
import com.uamishop.ventas.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas de integración para VentasApi")
class VentasApiIntegrationTest {

    @Autowired
    private VentasApi ventasApi;

    @Autowired
    private CarritoRepository carritoRepository;  // Repositorio real para preparar datos

    private UUID carritoIdExistente;
    private UUID carritoIdCompletado;
    private UUID clienteId;
    private UUID productoId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        productoId = UUID.randomUUID();

        Carrito carritoActivo = new Carrito(new ClienteId(clienteId));
        carritoActivo.agregarItem(
                new ProductoId(productoId),
                "Producto Test",
                2,
                new Money(new BigDecimal("100.00"))
        );
        carritoActivo = carritoRepository.save(carritoActivo);
        carritoIdExistente = carritoActivo.getId().getValue();  

        Carrito carritoCompletado = new Carrito(new ClienteId(clienteId));
        carritoCompletado.agregarItem(
                new ProductoId(productoId),
                "Otro Producto",
                1,
                new Money(new BigDecimal("50.00"))
        );
        carritoCompletado.completarCheckout(); 
        carritoCompletado = carritoRepository.save(carritoCompletado);
        carritoIdCompletado = carritoCompletado.getId().getValue();
    }

    @Test
    @DisplayName("obtenerResumen: debe retornar el resumen de un carrito existente")
    void obtenerResumen_existente_retornaCarritoResumen() {
        CarritoResumen resumen = ventasApi.obtenerResumen(carritoIdExistente);

        assertThat(resumen).isNotNull();
        assertThat(resumen.carritoId()).isEqualTo(carritoIdExistente);
        assertThat(resumen.clienteId().getValue()).isEqualTo(clienteId);
        assertThat(resumen.estado()).isEqualTo("ACTIVO");  

        assertThat(resumen.items()).hasSize(1);
        ItemCarritoResumen item = resumen.items().get(0);
        assertThat(item.productoId().getValue()).isEqualTo(productoId);
        assertThat(item.cantidad()).isEqualTo(2);
        assertThat(item.precioUnitario().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("obtenerResumen: debe retornar null (o lanzar excepción) si el carrito no existe")
    void obtenerResumen_noExistente_retornaNull() {
        UUID idInexistente = UUID.randomUUID();
        CarritoResumen resumen = ventasApi.obtenerResumen(idInexistente);
        assertThat(resumen).isNull();
       
    }

    @Test
    @DisplayName("completarCheckout: debe cambiar el estado del carrito a completado")
    void completarCheckout_carritoActivo_cambiaEstado() {
        ventasApi.completarCheckout(carritoIdExistente);

     
        Carrito carritoActualizado = carritoRepository.findById(new CarritoId(carritoIdExistente)).orElseThrow();
        assertThat(carritoActualizado.getEstado().name()).isEqualTo("COMPLETADO");  
    }

    @Test
    @DisplayName("completarCheckout: debe lanzar excepción si el carrito ya está completado")
    void completarCheckout_carritoYaCompletado_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> {
            ventasApi.completarCheckout(carritoIdCompletado);
        });
    }

    @Test
    @DisplayName("completarCheckout: debe lanzar excepción si el carrito no existe")
    void completarCheckout_noExistente_lanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            ventasApi.completarCheckout(idInexistente);
        });
    }
}