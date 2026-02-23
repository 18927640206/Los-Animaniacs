package com.uamishop.ordenes.domain;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ClienteId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Orden {
    private final OrdenId id;
    private final ClienteId clienteId;
    private EstadoOrden estado;
    private final List<ItemOrden> items;
    private final DireccionEnvio direccionEnvio;
    private final LocalDateTime fechaCreacion;
    private final List<CambioEstado> historialEstados;

    public Orden(OrdenId id, ClienteId clienteId, List<ItemOrden> items, 
                 DireccionEnvio direccionEnvio) {
        // RN-ORD-01
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Debe tener items");
        }
        
        this.id = id;
        this.clienteId = clienteId;
        this.items = new ArrayList<>(items);
        this.direccionEnvio = direccionEnvio;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.historialEstados = new ArrayList<>();
        
        registrarCambioEstado(null, EstadoOrden.PENDIENTE, "Orden creada");
    }

    public void confirmar() {
        // RN-ORD-05
        if (estado != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("Solo se puede confirmar si está PENDIENTE");
        }
        
        estado = EstadoOrden.CONFIRMADA;
        registrarCambioEstado(EstadoOrden.PENDIENTE, EstadoOrden.CONFIRMADA, "Confirmada");
    }

    public void marcarEnviada(String numeroGuia) {
        // RN-ORD-10
        if (estado != EstadoOrden.EN_PREPARACION) {
            throw new IllegalStateException("Debe estar EN_PREPARACION");
        }
        
        // RN-ORD-11 y RN-ORD-12
        if (numeroGuia == null || numeroGuia.length() < 10) {
            throw new IllegalArgumentException("Número de guía inválido");
        }
        
        estado = EstadoOrden.ENVIADA;
        registrarCambioEstado(EstadoOrden.EN_PREPARACION, EstadoOrden.ENVIADA, 
                            "Enviada: " + numeroGuia);
    }

    public void cancelar(String motivo) {
        // RN-ORD-14
        if (estado == EstadoOrden.ENVIADA || estado == EstadoOrden.ENTREGADA) {
            throw new IllegalStateException("No se puede cancelar");
        }
        
        // RN-ORD-15 y RN-ORD-16
        if (motivo == null || motivo.length() < 10) {
            throw new IllegalArgumentException("Motivo inválido");
        }
        
        EstadoOrden anterior = estado;
        estado = EstadoOrden.CANCELADA;
        registrarCambioEstado(anterior, EstadoOrden.CANCELADA, motivo);
    }

    private void registrarCambioEstado(EstadoOrden anterior, EstadoOrden nuevo, String motivo) {
        historialEstados.add(new CambioEstado(anterior, nuevo, motivo));
    }

    public Money calcularTotal() {
        return items.stream()
            .map(ItemOrden::calcularSubtotal)
            .reduce(new Money(new BigDecimal("0"), "MXN"), Money::sumar);
    }
    
    // Getters
    public OrdenId getId() { return id; }
    public EstadoOrden getEstado() { return estado; }
    public List<ItemOrden> getItems() { return new ArrayList<>(items); }
    public List<CambioEstado> getHistorialEstados() { return new ArrayList<>(historialEstados); }
}
