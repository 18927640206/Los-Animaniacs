package com.uamishop.catalogo;

import com.uamishop.catalogo.api.CatalogoApi;
import com.uamishop.catalogo.api.ProductoDetalle;
import com.uamishop.catalogo.domain.Producto;
import com.uamishop.catalogo.domain.Categoria;
import com.uamishop.catalogo.repository.ProductoRepository;
import com.uamishop.catalogo.repository.CategoriaRepository;
import com.uamishop.shared.domain.Money; 
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
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private UUID productoIdExistente;
    private UUID productoIdSinStock;
    private UUID categoriaId;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos");
        categoria = categoriaRepository.save(categoria);
        categoriaId = categoria.getId().getValue();
        Producto producto = new Producto(
                "Laptop Gamer",
                "Alta gama",
                new Money(new BigDecimal("1500.00")),
                categoria,
                10 // Campo stock, si existe
        );
        producto = productoRepository.save(producto);
        productoIdExistente = producto.getId().getValue();

        // Crear un producto sin stock (0 unidades)
        Producto productoSinStock = new Producto(
                "Monitor 4K",
                "Monitor de alta resolución",
                new Money(new BigDecimal("300.00")),
                categoria,
                0
        );
        productoSinStock = productoRepository.save(productoSinStock);
        productoIdSinStock = productoSinStock.getId().getValue();
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
    @DisplayName("obtenerDetalleProducto: debe marcar isDisponible=false si stock es cero")
    void obtenerDetalleProducto_sinStock_isDisponibleFalse() {
        Optional<ProductoDetalle> resultado = catalogoApi.obtenerDetalleProducto(productoIdSinStock);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().isDisponible()).isFalse();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar true cuando hay suficiente stock")
    void hayStockDisponible_suficienteStock_retornaTrue() {
        boolean disponible = catalogoApi.hayStockDisponible(productoIdExistente, 5);
        assertThat(disponible).isTrue();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar false cuando no hay suficiente stock")
    void hayStockDisponible_insuficienteStock_retornaFalse() {
        boolean disponible = catalogoApi.hayStockDisponible(productoIdExistente, 15); // solo 10
        assertThat(disponible).isFalse();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar false si producto no existe")
    void hayStockDisponible_productoInexistente_retornaFalse() {
        boolean disponible = catalogoApi.hayStockDisponible(UUID.randomUUID(), 1);
        assertThat(disponible).isFalse();
    }

    @Test
    @DisplayName("hayStockDisponible: debe retornar false si stock es cero")
    void hayStockDisponible_stockCero_retornaFalse() {
        boolean disponible = catalogoApi.hayStockDisponible(productoIdSinStock, 1);
        assertThat(disponible).isFalse();
    }
}