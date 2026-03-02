package com.uamishop.ventas.domain;

import com.uamishop.shared.domain.ProductoId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.io.Serializable;

@Embeddable
public class ProductoRef implements Serializable {
        
        @Embedded
        private ProductoId productoId;
        
        private String nombre;
        private String sku;

        // Constructor vacío para JPA
        protected ProductoRef() {}

        public ProductoRef(ProductoId productoId, String nombre, String sku) {
            // Validación de regla de negocio RN-VO-05
            if (sku == null || !sku.matches("[A-Z]{3}-\\d{3}")) {
                throw new IllegalArgumentException("SKU inválido: debe tener el formato AAA-123");
            }
            this.productoId = productoId;
            this.nombre = nombre;
            this.sku = sku;
        }
        
        public ProductoId getProductoId() { 
            return productoId; 
        }

        public String getNombre() { 
            return nombre; 
        }

        // CORRECCIÓN: Método faltante que causaba el error en CarritoService
        public String getSku() { 
            return sku; 
        }
}