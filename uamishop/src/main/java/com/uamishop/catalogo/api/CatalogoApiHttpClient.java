package com.uamishop.catalogo.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador que permite al monolito seguir usando CatalogoApi,
 * pero redirigiendo la consulta al microservicio externo vía REST.
 */
@Component
public class CatalogoApiHttpClient implements CatalogoApi {

    private final RestTemplate restTemplate;
    private final String catalogoBaseUrl;

    public CatalogoApiHttpClient(RestTemplate restTemplate, 
                                @Value("${catalogo.service.url}") String catalogoBaseUrl) {
        this.restTemplate = restTemplate;
        this.catalogoBaseUrl = catalogoBaseUrl;
    }

    @Override
    public Optional<ProductoDetalle> obtenerDetalleProducto(UUID productoId) {
        // Nota: Asegúrate de que el path coincida con el controlador del microservicio (/api/v2/productos)
        String url = catalogoBaseUrl + "/api/v2/productos/" + productoId;
        try {
            ResponseEntity<ProductoDetalle> response = restTemplate.getForEntity(url, ProductoDetalle.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            // Log de error opcional
        }
        return Optional.empty();
    }

    @Override
    public boolean hayStockDisponible(UUID productoId, int cantidad) {
        String url = catalogoBaseUrl + "/api/v2/productos/" + productoId + "/stock?cantidad=" + cantidad;
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            return false;
        }
    }
}