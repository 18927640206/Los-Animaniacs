package com.uamishop.ventas.domain;
import com.uamishop.catalogo.domain.ProductoId;

public class ProductoRef {
	    private final ProductoId productoId;
	    private final String nombre;
	    private final String sku;

	    public ProductoRef(ProductoId productoId, String nombre, String sku) {
	        // RN-VO-05
	        if (!sku.matches("[A-Z]{3}-\\d{3}")) {
	            throw new IllegalArgumentException("SKU inválido");
	        }
	        this.productoId = productoId;
	        this.nombre = nombre;
	        this.sku = sku;
	    }
	    
	    public ProductoId getProductoId() { return productoId; }
	    public String getNombre() { return nombre; }
}
