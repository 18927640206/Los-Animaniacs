package com.uamishop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// creada para punto 1.2 de P8

@Configuration
public class RabbitConfig {
    public static final String EVENTS_EXCHANGE = "uamishop.events";
    public static final String QUEUE_CATALOGO_PRODUCTO_COMPRADO = "catalogo.producto-comprado";
    public static final String RK_PRODUCTO_COMPRADO = "producto.comprado";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE);
    }

    @Bean
    public Queue catalogoProductoCompradoQueue() {
        return new Queue(QUEUE_CATALOGO_PRODUCTO_COMPRADO, true);
    }

    @Bean
    public Binding catalogoProductoCompradoBinding(Queue catalogoProductoCompradoQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(catalogoProductoCompradoQueue)
                .to(eventsExchange)
                .with(RK_PRODUCTO_COMPRADO);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}