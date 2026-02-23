package com.uamishop.ordenes.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrdenResponse {

    private UUID id;
    private String clienteId;
    private String estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    private DireccionEnvioResponse direccionEnvio; // <--- Corregido
    private List<ItemOrdenResponse> items;

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public DireccionEnvioResponse getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(DireccionEnvioResponse direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    
    public List<ItemOrdenResponse> getItems() { return items; }
    public void setItems(List<ItemOrdenResponse> items) { this.items = items; }
}