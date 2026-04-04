package com.uamishop.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    
    @GetMapping("/catalogo")
    public ResponseEntity<Map<String, Object>> catalogoFallback() {
        return createFallbackResponse("Servicio de catálogo no disponible temporalmente");
    }
    
    @GetMapping("/ordenes")
    public ResponseEntity<Map<String, Object>> ordenesFallback() {
        return createFallbackResponse("Servicio de órdenes no disponible temporalmente");
    }
    
    @GetMapping("/ventas")
    public ResponseEntity<Map<String, Object>> ventasFallback() {
        return createFallbackResponse("Servicio de ventas no disponible temporalmente");
    }
    
    @GetMapping("/carritos")
    public ResponseEntity<Map<String, Object>> carritosFallback() {
        return createFallbackResponse("Servicio de carritos no disponible temporalmente");
    }
    
    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", message);
        response.put("path", "/fallback");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}