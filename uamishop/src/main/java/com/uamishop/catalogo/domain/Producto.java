package com.uamishop.catalogo.domain;

import com.uamishop.shared.domain.Money;
import jakarta.persistence.*; //importante para JPA
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "producto")
public class Producto {

   @EmbeddedId //Clave primaria compuesta/VO
    private ProductoId id;

    private String nombre;
    private String descripcion;

    @Embedded // El objeto Money se guardará en columnas monto y moneda
    @AttributeOverrides({
        @AttributeOverride(name = "monto", column = @Column(name = "precio_monto")),
        @AttributeOverride(name = "moneda", column = @Column(name = "precio_moneda"))
    })
    private Money precio;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "categoria_id"))
    private CategoriaId categoriaId;

    private boolean disponible;

    @ElementCollection // Para guardar la lista de imágenes como una tabla secundaria
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_id"))
    private List<Imagen> imagenes = new ArrayList<>();

    //JPA REQUIERE un constructor protegido o público sin argumentos
    protected Producto() {
        //this.imagenes = new ArrayList<>();
    }

    public Producto(ProductoId id, String nombre, String descripcion, 
                   Money precio, CategoriaId categoriaId) {
        // RN-CAT-01
        if (nombre.length() < 3 || nombre.length() > 100) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        
        // RN-CAT-02
        if (precio.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio debe ser > 0");
        }

        // RN-CAT-03
        if (descripcion != null && descripcion.length() > 500) {
            throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
        }
        
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
        this.disponible = false;
        this.imagenes = new ArrayList<>();
    }

    public void cambiarPrecio(Money nuevoPrecio) {
        // RN-CAT-04
        if (nuevoPrecio.getMonto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Precio no puede ser negativo");
        }
        
        // RN-CAT-05
        BigDecimal maximo = precio.getMonto().multiply(new BigDecimal("1.5"));
        if (nuevoPrecio.getMonto().compareTo(maximo) > 0) {
            throw new IllegalArgumentException("No puede aumentar más del 50%");
        }
        
        this.precio = nuevoPrecio;
    }

    public void agregarImagen(Imagen imagen) {
        // RN-CAT-06
        if (imagenes.size() >= 5) {
            throw new IllegalStateException("Máximo 5 imágenes");
        }
        imagenes.add(imagen);
    }

    public void desactivar() {
        // RN-CAT-08
        if (!disponible) {
            throw new IllegalStateException("Ya está desactivado");
        }
        disponible = false;
    }

    public void activar() {
        // RN-CAT-09
        if (imagenes.isEmpty()) {
            throw new IllegalStateException("Necesita al menos una imagen");
        }
        
        // RN-CAT-10
        if (precio.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Precio debe ser > 0");
        }
        
        disponible = true;
    }
    
    // Getters
    public ProductoId getId() { return id; }
    public String getNombre() { return nombre; }
    public Money getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
    public List<Imagen> getImagenes() { return new ArrayList<>(imagenes); }
    public String getDescripcion() { return descripcion; }
    public CategoriaId getCategoriaId() { return categoriaId; }
}