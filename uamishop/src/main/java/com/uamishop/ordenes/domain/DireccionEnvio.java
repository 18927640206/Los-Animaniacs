package com.uamishop.ordenes.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class DireccionEnvio {
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    private String codigoPostal;
    private String pais;
    private String telefono;

    // Constructor vacío para JPA
    protected DireccionEnvio() {}

    public DireccionEnvio(String calle, String colonia, String ciudad, String estado,
                         String codigoPostal, String pais, String telefono) {
        // RN-VO-03
        if (calle == null || calle.trim().isEmpty()) {
            throw new IllegalArgumentException("Calle es obligatoria");
        }
        
        // RN-ORD-03
        if (codigoPostal == null || !codigoPostal.matches("\\d{5}")) {
            throw new IllegalArgumentException("Código postal inválido");
        }
        
        // RN-VO-04
        if (!"México".equalsIgnoreCase(pais)) {
            throw new IllegalArgumentException("Solo México");
        }
        
        // RN-ORD-04
        if (telefono == null || !telefono.matches("\\d{10}")) {
            throw new IllegalArgumentException("Teléfono inválido");
        }
        
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
        this.telefono = telefono;
    }
    
    public String getCodigoPostal() { return codigoPostal; }
    public String getTelefono() { return telefono; }
}