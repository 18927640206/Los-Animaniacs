package com.uamishop.ventas.api;

import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;
import com.uamishop.ventas.domain.Carrito;
import com.uamishop.ventas.domain.ProductoRef;
import com.uamishop.ventas.repository.CarritoJpaRepository;
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
    private CarritoJpaRepository carritoJpaRepository; 

    private UUID carritoIdExistente;
    private UUID carritoIdParaCompletar;
    private UUID carritoIdCompletado;
    private String clienteIdStr;
    private String productoIdStr;

    @BeforeEach
    void setUp() {
        clienteIdStr = UUID.randomUUID().toString();
        productoIdStr = UUID.randomUUID().toString();

        // 1. Carrito ACTIVO (solo tiene productos, no se puede completar checkout aún)
        carritoIdExistente = UUID.randomUUID();
        Carrito carritoActivo = new Carrito(carritoIdExistente.toString(), new ClienteId(clienteIdStr));
        carritoActivo.agregarProducto(
                new ProductoRef(new ProductoId(productoIdStr), "Producto Test", "SKU-001"),
                2,
                new Money(new BigDecimal("100.00"), "MXN")
        );
        carritoJpaRepository.save(carritoActivo);

        // 2. Carrito EN_CHECKOUT (listo para probar la acción completarCheckout)
        carritoIdParaCompletar = UUID.randomUUID();
        Carrito carritoEnCheckout = new Carrito(carritoIdParaCompletar.toString(), new ClienteId(clienteIdStr));
        carritoEnCheckout.agregarProducto(
                new ProductoRef(new ProductoId(productoIdStr), "Otro Producto", "SKU-002"),
                1,
                new Money(new BigDecimal("60.00"), "MXN") // RN-VEN-12 exige > $50
        );
        carritoEnCheckout.iniciarCheckout(); // Lo pasamos a EN_CHECKOUT
        carritoJpaRepository.save(carritoEnCheckout);
        
        // 3. Carrito COMPLETADO (para probar que falla si intentamos completarlo de nuevo)
        carritoIdCompletado = UUID.randomUUID();
        Carrito carritoCompletadoObj = new Carrito(carritoIdCompletado.toString(), new ClienteId(clienteIdStr));
        carritoCompletadoObj.agregarProducto(
                new ProductoRef(new ProductoId(productoIdStr), "Otro Producto 3", "SKU-003"),
                1,
                new Money(new BigDecimal("60.00"), "MXN")
        );
        carritoCompletadoObj.iniciarCheckout();
        carritoCompletadoObj.completarCheckout(); // Lo completamos
        carritoJpaRepository.save(carritoCompletadoObj);
    }

    @Test
    @DisplayName("obtenerResumen: debe retornar el resumen de un carrito existente")
    void obtenerResumen_existente_retornaCarritoResumen() {
        CarritoResumen resumen = ventasApi.obtenerResumen(carritoIdExistente);

        assertThat(resumen).isNotNull();
        // Usamos los métodos de acceso del Record según el nombre de la variable
        assertThat(resumen.carritoId()).isEqualTo(carritoIdExistente);
        assertThat(resumen.clienteId().getId()).isEqualTo(clienteIdStr);
        assertThat(resumen.estado()).isEqualTo("ACTIVO");  

        assertThat(resumen.items()).hasSize(1);
        ItemCarritoResumen item = resumen.items().get(0);
        assertThat(item.productoId().getId()).isEqualTo(productoIdStr);
        assertThat(item.cantidad()).isEqualTo(2);
        assertThat(item.precioUnitario().getMonto()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("obtenerResumen: debe lanzar excepción si el carrito no existe")
    void obtenerResumen_noExistente_lanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            ventasApi.obtenerResumen(idInexistente);
        });
    }

    @Test
    @DisplayName("completarCheckout: debe cambiar el estado del carrito a completado")
    void completarCheckout_carritoEnCheckout_cambiaEstado() {
        ventasApi.completarCheckout(carritoIdParaCompletar);

        Carrito carritoActualizado = carritoJpaRepository.findById(carritoIdParaCompletar.toString()).orElseThrow();
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