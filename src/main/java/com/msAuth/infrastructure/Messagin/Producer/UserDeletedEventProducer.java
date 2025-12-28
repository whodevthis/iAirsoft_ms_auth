package com.msAuth.infrastructure.Messagin.Producer;

import com.msAuth.application.Event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class UserDeletedEventProducer {
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_AUDIT = "audit.exchange";
    private static final String ROUTING_KEY = "audit.deleteUser";

    public void send(UserEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE_AUDIT, ROUTING_KEY, event);
    }
}



