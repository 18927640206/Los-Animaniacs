package com.uamishop.ordenes.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Información geográfica para la entrega del pedido")
public class DireccionEnvioRequest {

    @Schema(description = "Nombre de la calle y número", example = "Av. Universidad 123")
    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @Schema(description = "Colonia o barrio", example = "Iztapalapa")
    @NotBlank(message = "La colonia es obligatoria")
    private String colonia;

    @Schema(description = "Ciudad o delegación", example = "CDMX")
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @Schema(description = "Estado de la república", example = "Ciudad de México")
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Schema(description = "Código postal (exactamente 5 dígitos)", example = "09340")
    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^\\d{5}$", message = "El código postal debe tener exactamente 5 dígitos") // RN-ORD-03
    private String codigoPostal;

    @Schema(description = "País (por ahora solo México)", example = "México")
    @NotBlank(message = "El país es obligatorio")
    @Pattern(regexp = "México", message = "Por ahora solo se realizan envíos en México") // RN-VO-04
    private String pais;

    @Schema(description = "Teléfono de contacto (10 dígitos)", example = "5512345678")
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe tener 10 dígitos numéricos") // RN-ORD-04
    private String telefono;

    // Getters y Setters
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}