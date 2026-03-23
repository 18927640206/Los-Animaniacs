package com.uamishop.ordenes.domain;
import java.time.LocalDateTime;

public class InfoPago {
	 private final String referencia;
	    private final EstadoPago estado;
	    private final LocalDateTime fechaProcesamiento;
	    private final String metodoPago;

	    public InfoPago(String referencia, EstadoPago estado, String metodoPago) {
	        if (referencia == null || referencia.trim().isEmpty()) {
	            throw new IllegalArgumentException("La referencia de pago no puede estar vacía");
	        }
	        
	        this.referencia = referencia;
	        this.estado = estado;
	        this.fechaProcesamiento = LocalDateTime.now();
	        this.metodoPago = metodoPago != null ? metodoPago : "";
	    }

	    // Getters
	    public String getReferencia() { return referencia; }
	    public EstadoPago getEstado() { return estado; }
	    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
	    public String getMetodoPago() { return metodoPago; }

}
