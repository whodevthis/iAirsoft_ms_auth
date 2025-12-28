package com.msAuth.infrastructure.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de Exchanges
    public static final String AUDIT_EXCHANGE = "audit.exchange";

    // Nombres de Queues
    public static final String AUDIT_QUEUE = "audit.general.queue";
    public static final String EXCEPTION_QUEUE = "audit.exception.queue";

    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange(AUDIT_EXCHANGE);
    }

    @Bean
    public Queue auditQueue() {
        return new Queue(AUDIT_QUEUE);
    }

    @Bean
    public Queue exceptionQueue() {
        return new Queue(EXCEPTION_QUEUE);
    }


    @Bean
    public Binding auditBinding(Queue auditQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with("audit.*");
    }

    // Binding específico para excepciones si quieres que vayan a otra cola
    @Bean
    public Binding exceptionBinding(Queue exceptionQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(exceptionQueue).to(auditExchange).with("audit.exception");
    }

    // TRADUCTOR A JSON: Indispensable para enviar objetos
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}