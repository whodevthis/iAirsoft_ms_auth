package com.msAuth.infrastructure.Messagin.Producer;

import com.msAuth.application.Event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoggedInEventProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "notification.exchange";
    private static final String ROUTING_KEY = "notification.login";

    public void send(UserEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
    }
}