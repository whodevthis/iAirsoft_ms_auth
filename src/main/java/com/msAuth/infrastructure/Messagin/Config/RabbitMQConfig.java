package com.msAuth.infrastructure.Messagin.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String AUTH_EXCHANGE         = "auth.exchange";

    // Queues
    public static final String PASSWORD_UPDATE_QUEUE = "auth.password.update.queue";

    // ── auth.exchange ────────────────────────────────────────
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

    // ── notification.exchange ────────────────────────────────
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    // ── Converter ────────────────────────────────────────────
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}