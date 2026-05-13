package com.msAuth.infrastructure.Messagin.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String AUDIT_EXCHANGE        = "audit.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String AUTH_EXCHANGE         = "auth.exchange";

    // Queues
    public static final String EXCEPTION_QUEUE       = "audit.exception.queue";
    public static final String PASSWORD_UPDATE_QUEUE = "auth.password.update.queue";

    // ── audit.exchange ──────────────────────────────────────
    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange(AUDIT_EXCHANGE);
    }

    @Bean
    public Queue exceptionQueue() {
        return QueueBuilder.durable(EXCEPTION_QUEUE).build();
    }

    @Bean
    public Binding exceptionBinding(Queue exceptionQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(exceptionQueue).to(auditExchange).with("audit.exception");
    }

    // ── notification.exchange ───────────────────────────────
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    // ── auth.exchange (recibe desde msNotifications) ────────
    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(AUTH_EXCHANGE);
    }

    @Bean
    public Queue passwordUpdateQueue() {
        return QueueBuilder.durable(PASSWORD_UPDATE_QUEUE).build();
    }

    @Bean
    public Binding passwordUpdateBinding(Queue passwordUpdateQueue, TopicExchange authExchange) {
        return BindingBuilder
                .bind(passwordUpdateQueue)
                .to(authExchange)
                .with("auth.password.update");
    }

    // ── Converter ───────────────────────────────────────────
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}