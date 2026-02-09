package com.uamishop.ordenes.domain;

public class DirreccionEnvio {
	private final String calle;
    private final String colonia;
    private final String ciudad;
    private final String estado;
    private final String codigoPostal;
    private final String pais;
    private final String telefono;

    public DireccionEnvio(String calle, String colonia, String ciudad, String estado,
                         String codigoPostal, String pais, String telefono) {
        // RN-VO-03
        if (calle == null || calle.trim().isEmpty()) {
            throw new IllegalArgumentException("Calle es obligatoria");
        }
        
        // RN-ORD-03
        if (!codigoPostal.matches("\\d{5}")) {
            throw new IllegalArgumentException("Código postal inválido");
        }
        
        // RN-VO-04
        if (!"México".equalsIgnoreCase(pais)) {
            throw new IllegalArgumentException("Solo México");
        }
        
        // RN-ORD-04
        if (!telefono.matches("\\d{10}")) {
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
