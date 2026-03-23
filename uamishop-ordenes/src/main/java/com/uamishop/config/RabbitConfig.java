// Archivo: /workspaces/Los-Animaniacs/uamishop-ordenes/src/main/java/com/uamishop/config/RabbitConfig.java

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
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitConfig {
    public static final String EVENTS_EXCHANGE = "uamishop.events";
    public static final String QUEUE_CATALOGO_PRODUCTO_COMPRADO = "catalogo.producto-comprado";
    public static final String QUEUE_CATALOGO_PRODUCTO_AGREGADO = "catalogo.producto-agregado-carrito";
    public static final String RK_PRODUCTO_COMPRADO = "producto.comprado";
    public static final String RK_PRODUCTO_AGREGADO = "producto.agregado-carrito";
    public static final String QUEUE_VENTAS_ORDEN_CREADA = "ventas.orden-creada";
    public static final String RK_ORDEN_CREADA = "orden.creada";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE);
    }

    @Bean
    public Queue catalogoProductoCompradoQueue() {
        return new Queue(QUEUE_CATALOGO_PRODUCTO_COMPRADO, true);
    }

    @Bean
    public Queue catalogoProductoAgregadoQueue() {
       return new Queue(QUEUE_CATALOGO_PRODUCTO_AGREGADO, true);
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
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                        Jackson2JsonMessageConverter messageConverter) {
       RabbitTemplate template = new RabbitTemplate(connectionFactory);
       template.setMessageConverter(messageConverter);
       return template;
   }
   @Bean
   public Queue ventasOrdenCreadaQueue() {
    return new Queue(QUEUE_VENTAS_ORDEN_CREADA, true);
    }
    
    @Bean
    public Binding ventasOrdenCreadaBinding(Queue ventasOrdenCreadaQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(ventasOrdenCreadaQueue)
        .to(eventsExchange)
        .with(RK_ORDEN_CREADA);
        }
}