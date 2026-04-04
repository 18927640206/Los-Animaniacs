// uamishop-ordenes/src/main/java/com/uamishop/ordenes/domain/Orden.java
package com.uamishop.ordenes.domain;

import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ClienteId;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @Column(name = "id")
    private String id;

    @Transient
    private OrdenId ordenId;

    @Embedded
    // AJUSTE 1: Cambiamos 'value' por 'id' para que coincida con tu clase ClienteId
    @AttributeOverride(name = "id", column = @Column(name = "cliente_id"))
    private ClienteId clienteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_orden") // Renombramos esta columna por seguridad
    private EstadoOrden estado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orden_items", joinColumns = @JoinColumn(name = "orden_id"))
    private List<ItemOrden> items;

    @Embedded
    // AJUSTE 2: Renombramos la columna 'estado' de la dirección para que no choque con la orden
    @AttributeOverride(name = "estado", column = @Column(name = "direccion_estado"))
    private DireccionEnvio direccionEnvio;

    private LocalDateTime fechaCreacion;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "orden_historial", joinColumns = @JoinColumn(name = "orden_id"))
    private List<CambioEstado> historialEstados;

    protected Orden() {
    }

    public Orden(OrdenId id, ClienteId clienteId, List<ItemOrden> items, 
                 DireccionEnvio direccionEnvio) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Debe tener items");
        }

        this.ordenId = id;
        this.id = id != null ? id.getId() : null;

        this.clienteId = clienteId;
        this.items = new ArrayList<>(items);
        this.direccionEnvio = direccionEnvio;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.historialEstados = new ArrayList<>();

        registrarCambioEstado(null, EstadoOrden.PENDIENTE, "Orden creada");

        if (calcularTotal().getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total de la orden debe ser mayor a cero");
        }
    }

    public void confirmar() {
        if (estado != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("Solo se puede confirmar si está PENDIENTE");
        }
        estado = EstadoOrden.CONFIRMADA;
        registrarCambioEstado(EstadoOrden.PENDIENTE, EstadoOrden.CONFIRMADA, "Confirmada");
    }

    public void procesarPago(String referenciaPago) {
        if (estado != EstadoOrden.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede procesar pago si la orden está CONFIRMADA");
        }
        if (referenciaPago == null || referenciaPago.trim().isEmpty()) {
            throw new IllegalArgumentException("La referencia de pago no puede estar vacía");
        }
        registrarCambioEstado(estado, EstadoOrden.PAGO_PROCESADO, "Pago procesado con ref: " + referenciaPago);
        estado = EstadoOrden.PAGO_PROCESADO;
    }

    public void marcarEnProceso() {
        if (estado != EstadoOrden.PAGO_PROCESADO) {
            throw new IllegalStateException("Solo se puede marcar en proceso si el pago fue procesado");
        }
        registrarCambioEstado(estado, EstadoOrden.EN_PREPARACION, "Orden en preparación");
        estado = EstadoOrden.EN_PREPARACION;
    }

    public void marcarEnviada(String numeroGuia) {
        if (estado != EstadoOrden.EN_PREPARACION) {
            throw new IllegalStateException("Debe estar EN_PREPARACION");
        }
        if (numeroGuia == null || numeroGuia.length() < 10) {
            throw new IllegalArgumentException("Número de guía inválido");
        }
        estado = EstadoOrden.ENVIADA;
        registrarCambioEstado(EstadoOrden.EN_PREPARACION, EstadoOrden.ENVIADA, "Enviada: " + numeroGuia);
    }

    public void marcarEntregada() {
        if (estado != EstadoOrden.ENVIADA && estado != EstadoOrden.EN_TRANSITO) {
            throw new IllegalStateException("Solo se puede marcar entregada si está ENVIADA o EN_TRANSITO");
        }
        registrarCambioEstado(estado, EstadoOrden.ENTREGADA, "Orden entregada al cliente");
        estado = EstadoOrden.ENTREGADA;
    }

    public void cancelar(String motivo) {
        // CORRECCIÓN: Agregamos el chequeo de si ya está CANCELADA
        if (estado == EstadoOrden.ENVIADA || estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA) {
            throw new IllegalArgumentException("No se puede cancelar la orden en su estado actual: " + estado);
            }
            
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

    public OrdenId getId() { 
        if (this.ordenId == null && this.id != null) {
            this.ordenId = new OrdenId(this.id);
        }
        return this.ordenId; 
    }
    public EstadoOrden getEstado() { return estado; }
    public List<ItemOrden> getItems() { return new ArrayList<>(items); }
    public List<CambioEstado> getHistorialEstados() { return new ArrayList<>(historialEstados); }
    public ClienteId getClienteId() { return clienteId; }
}