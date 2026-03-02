package com.uamishop.catalogo.api;

import com.uamishop.catalogo.domain.Categoria;
import com.uamishop.catalogo.domain.Imagen;
import com.uamishop.catalogo.domain.Producto;
import com.uamishop.catalogo.repository.CategoriaJpaRepository;
import com.uamishop.catalogo.repository.ProductoJpaRepository;
import com.uamishop.shared.domain.CategoriaId;
import com.uamishop.shared.domain.Money;
import com.uamishop.shared.domain.ProductoId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Pruebas de integración para CatalogoApi")
class CatalogoApiIntegrationTest {

    @Autowired
    private CatalogoApi catalogoApi;

    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    @Autowired
    private CategoriaJpaRepository categoriaJpaRepository;

    private UUID productoIdExistente;
    private UUID productoIdSinStock;
    private CategoriaId categoriaId;

    @BeforeEach
    void setUp() {
        // 1. Creamos la categoría con su Value Object ID correspondiente
        categoriaId = new CategoriaId(UUID.randomUUID().toString());
        Categoria categoria = new Categoria(categoriaId, "Electrónica");
        categoria.actualizarDescripcion("Productos electrónicos");
        categoriaJpaRepository.save(categoria);

        // 2. Creamos un producto Activo/Disponible
        ProductoId prodId1 = new ProductoId(UUID.randomUUID().toString());
        Producto producto = new Producto(
                prodId1,
                "Laptop Gamer",
                "Alta gama",
                new Money(new BigDecimal("1500.00"), "MXN"),
                categoriaId
        );
        // Para que esté disponible, aplicamos las reglas de negocio RN-CAT-09 y RN-CAT-10
        producto.agregarImagen(new Imagen("http://example.com/lap.jpg", "Frente"));
        producto.activar(); // Cambia isDisponible a true
        productoJpaRepository.save(producto);
        productoIdExistente = UUID.fromString(prodId1.getId());

        // 3. Creamos un producto Inactivo/Sin Stock (0 unidades)
        ProductoId prodId2 = new ProductoId(UUID.randomUUID().toString());
        Producto productoSinStock = new Producto(
                prodId2,
                "Monitor 4K",
                "Monitor de alta resolución",
                new Money(new BigDecimal("300.00"), "MXN"),
                categoriaId
        );
        // Al no llamar a .activar(), su estado isDisponible() se queda en false
        productoJpaRepository.save(productoSinStock);
        productoIdSinStock = UUID.fromString(prodId2.getId());
    }

    @Test
    @DisplayName("obtenerDetalleProducto: debe retornar Optional con producto cuando existe")
    void obtenerDetalleProducto_existente_retornaProducto() {
        Optional<ProductoDetalle> resultado = catalogoApi.obtenerDetalleProducto(productoIdExistente);

        assertThat(resultado).isPresent();
        ProductoDetalle detalle = resultado.get();
        assertThat(detalle.id()).isEqualTo(productoIdExistente);
        assertThat(detalle.nombre()).isEqualTo("Laptop Gamer");
        assertThat(detalle.precio()).isEqualByComparingTo("1500.00");
        assertThat(detalle.isDisponible()).isTrue();
    }

    @Test
    @DisplayName("obtenerDetalleProducto: debe retornar Optional vacío cuando no existe")
    void obtenerDetalleProducto_noExistente_retornaVacio() {
        UUID idInexistente = UUID.randomUUID();
        Optional<ProductoDetalle> resultado = catalogoApi.obtenerDetalleProducto(idInexistente);
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("obtenerDetalleProducto: debe marcar isDisponible=false si el producto no está activo")
    void obtenerDetalleProducto_sinStock_isDisponibleFalse() {
        Optional<ProductoDetalle> resultado = catalogoApi.obtenerDetalleProducto(productoIdSinStock);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().isDisponible()).isFalse();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar true cuando hay disponibilidad")
    void hayStockDisponible_suficienteStock_retornaTrue() {
        boolean disponible = catalogoApi.hayStockDisponible(productoIdExistente, 5);
        assertThat(disponible).isTrue();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar false si producto no existe")
    void hayStockDisponible_productoInexistente_retornaFalse() {
        boolean disponible = catalogoApi.hayStockDisponible(UUID.randomUUID(), 1);
        assertThat(disponible).isFalse();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar false si no está disponible")
    void hayStockDisponible_stockCero_retornaFalse() {
        boolean disponible = catalogoApi.hayStockDisponible(productoIdSinStock, 1);
        assertThat(disponible).isFalse();
    }
}