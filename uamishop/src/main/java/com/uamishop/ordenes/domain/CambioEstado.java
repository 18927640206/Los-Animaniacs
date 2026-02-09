package com.uamishop.ordenes.domain;
import java.time.LocalDateTime;

public class CambioEstado {
	private final EstadoOrden estadoAnterior;
    private final EstadoOrden nuevoEstado;
    private final LocalDateTime fecha;
    private final String motivo;

    public CambioEstado(EstadoOrden estadoAnterior, EstadoOrden nuevoEstado, String motivo) {
        this.estadoAnterior = estadoAnterior;
        this.nuevoEstado = nuevoEstado;
        this.fecha = LocalDateTime.now();
        this.motivo = motivo;
    }
    
    public EstadoOrden getNuevoEstado() { return nuevoEstado; }
    public String getMotivo() { return motivo; }

}
