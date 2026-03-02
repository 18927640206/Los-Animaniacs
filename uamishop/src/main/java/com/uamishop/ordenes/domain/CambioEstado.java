package com.uamishop.ordenes.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
public class CambioEstado {
    
    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoAnterior;
    
    @Enumerated(EnumType.STRING)
    private EstadoOrden nuevoEstado;
    
    private LocalDateTime fecha;
    private String motivo;

    // Constructor vacío requerido por JPA/Hibernate
    protected CambioEstado() {
    }

    public CambioEstado(EstadoOrden estadoAnterior, EstadoOrden nuevoEstado, String motivo) {
        this.estadoAnterior = estadoAnterior;
        this.nuevoEstado = nuevoEstado;
        this.fecha = LocalDateTime.now();
        this.motivo = motivo;
    }
    
    // Getters
    public EstadoOrden getEstadoAnterior() { return estadoAnterior; }
    public EstadoOrden getNuevoEstado() { return nuevoEstado; }
    public LocalDateTime getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
}