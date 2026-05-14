package com.msAuth.infrastructure.Messagin.Producer;

import com.msAuth.application.Event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "notification.exchange";
    private static final String ROUTING_KEY = "notification.welcome";

    public void send(NotificationEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
    }
}