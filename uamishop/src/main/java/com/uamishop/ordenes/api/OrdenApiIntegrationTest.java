package com.uamishop.ordenes;

import com.uamishop.ordenes.api.OrdenApi;
import com.uamishop.ordenes.api.OrdenResumen;
import com.uamishop.ordenes.domain.Orden;
import com.uamishop.ordenes.domain.OrdenId;
import com.uamishop.ordenes.domain.OrdenEstado;
import com.uamishop.ordenes.repository.OrdenRepository;
import com.uamishop.shared.domain.ClienteId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private OrdenRepository ordenRepository;
    private UUID ordenIdActiva;
    private UUID ordenIdCancelada;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        Orden ordenActiva = new Orden(
                new ClienteId(clienteId),
                LocalDateTime.now(),
                OrdenEstado.CONFIRMADA, 
                new BigDecimal("250.00")
        );
        ordenActiva = ordenRepository.save(ordenActiva);
        ordenIdActiva = ordenActiva.getId().getValue(); 

        Orden ordenCancelada = new Orden(
                new ClienteId(clienteId),
                LocalDateTime.now(),
                OrdenEstado.CANCELADA,
                new BigDecimal("100.00")
        );
        ordenCancelada = ordenRepository.save(ordenCancelada);
        ordenIdCancelada = ordenCancelada.getId().getValue();
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
    @DisplayName("obtenerResumen: debe retornar null (o lanzar excepción) si la orden no existe")
    void obtenerResumen_noExistente_retornaNull() {
        UUID idInexistente = UUID.randomUUID();
        OrdenResumen resumen = ordenApi.obtenerResumen(idInexistente);
        assertThat(resumen).isNull();
    }

    @Test
    @DisplayName("cancelar: debe cancelar una orden activa")
    void cancelar_ordenActiva_cambiaEstado() {
        String motivo = "Cliente no confirmó pago";
        ordenApi.cancelar(ordenIdActiva, motivo);

        Orden ordenActualizada = ordenRepository.findById(new OrdenId(ordenIdActiva)).orElseThrow();
        assertThat(ordenActualizada.getEstado()).isEqualTo(OrdenEstado.CANCELADA);
    }

    @Test
    @DisplayName("cancelar: debe lanzar excepción si la orden ya está cancelada")
    void cancelar_ordenYaCancelada_lanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> {
            ordenApi.cancelar(ordenIdCancelada, "Motivo de prueba");
        });
    }

    @Test
    @DisplayName("cancelar: debe lanzar excepción si la orden no existe")
    void cancelar_ordenNoExistente_lanzaExcepcion() {
        UUID idInexistente = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            ordenApi.cancelar(idInexistente, "Motivo");
        });
    }
}