package com.uamishop.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("=== Gateway Request ===");
        log.info("Method: {}", exchange.getRequest().getMethod());
        log.info("URI: {}", exchange.getRequest().getURI());
        log.info("Headers: {}", exchange.getRequest().getHeaders());
        
        return chain.filter(exchange)
            .then(Mono.fromRunnable(() -> {
                log.info("=== Gateway Response ===");
                log.info("Status: {}", exchange.getResponse().getStatusCode());
            }));
    }
    
    @Override
    public int getOrder() {
        return -1; // Ejecutar antes que otros filtros
    }
}