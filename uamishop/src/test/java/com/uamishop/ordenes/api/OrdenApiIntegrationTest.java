package com.uamishop.ordenes.api;

import com.uamishop.ordenes.domain.*;
import com.uamishop.ordenes.repository.OrdenJpaRepository;
import com.uamishop.shared.domain.ClienteId;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;
import com.uamishop.ventas.domain.ProductoRef; // <-- Importamos ProductoRef para que empate con tu código
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas de integración para OrdenApi")
class OrdenApiIntegrationTest {

    @Autowired
    private OrdenApi ordenApi;

    @Autowired
    private OrdenJpaRepository ordenJpaRepository;

    private UUID ordenIdActiva;
    private UUID ordenIdCancelada;
    private String clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID().toString();
        
        // Preparamos los datos obligatorios para crear una orden válida (RN-ORD-01 y RN-ORD-02)
        DireccionEnvio direccion = new DireccionEnvio("Calle Falsa", "Centro", "CDMX", "CDMX", "12345", "México", "5512345678");
        
        // CORRECCIÓN: Instanciamos ProductoRef porque así lo exige el constructor actual de tu ItemOrden
        ProductoRef ref1 = new ProductoRef(new ProductoId(UUID.randomUUID().toString()), "Producto Demo 1", "SKU-001");
        ProductoRef ref2 = new ProductoRef(new ProductoId(UUID.randomUUID().toString()), "Producto Demo 2", "SKU-002");

        ItemOrden item1 = new ItemOrden(new ItemOrdenId(UUID.randomUUID().toString()), ref1, 1, new Money(new BigDecimal("250.00"), "MXN"));
        ItemOrden item2 = new ItemOrden(new ItemOrdenId(UUID.randomUUID().toString()), ref2, 1, new Money(new BigDecimal("100.00"), "MXN"));

        // Orden Activa (Confirmada)
        ordenIdActiva = UUID.randomUUID();
        Orden ordenActiva = new Orden(
                new OrdenId(ordenIdActiva.toString()),
                new ClienteId(clienteId),
                List.of(item1),
                direccion
        );
        ordenActiva.confirmar(); // Cambia el estado a CONFIRMADA
        ordenJpaRepository.save(ordenActiva);

        // Orden Cancelada
        ordenIdCancelada = UUID.randomUUID();
        Orden ordenCancelada = new Orden(
                new OrdenId(ordenIdCancelada.toString()),
                new ClienteId(clienteId),
                List.of(item2),
                direccion
        );
        ordenCancelada.cancelar("Motivo de cancelación válido"); // Cambia el estado a CANCELADA
        ordenJpaRepository.save(ordenCancelada);
    }

    @Test
    @DisplayName("obtenerResumen: debe retornar resumen de una orden existente")
    void obtenerResumen_existente_retornaResumen() {
        OrdenResumen resumen = ordenApi.obtenerResumen(ordenIdActiva);

        assertThat(resumen).isNotNull();
        assertThat(resumen.id()).isEqualTo(ordenIdActiva);
        assertThat(resumen.estado()).isEqualTo("CONFIRMADA"); 
        assertThat(resumen.total()).isEqualByComparingTo("250.00");
    }

    @Test
    @DisplayName("obtenerResumen: debe lanzar excepción si la orden no existe")
    void obtenerResumen_noExistente_lanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            ordenApi.obtenerResumen(idInexistente);
        });
    }

    @Test
    @DisplayName("cancelar: debe cancelar una orden activa")
    void cancelar_ordenActiva_cambiaEstado() {
        String motivo = "Cliente no confirmó el pago a tiempo";
        ordenApi.cancelar(ordenIdActiva, motivo);

        Orden ordenActualizada = ordenJpaRepository.findById(ordenIdActiva).orElseThrow();
        assertThat(ordenActualizada.getEstado()).isEqualTo(EstadoOrden.CANCELADA);
    }

    @Test
    @DisplayName("cancelar: debe lanzar excepción si la orden ya está cancelada")
    void cancelar_ordenYaCancelada_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> {
            ordenApi.cancelar(ordenIdCancelada, "Motivo de prueba largo");
        });
    }

    @Test
    @DisplayName("cancelar: debe lanzar excepción si la orden no existe")
    void cancelar_ordenNoExistente_lanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            ordenApi.cancelar(idInexistente, "Motivo válido y largo");
        });
    }
}