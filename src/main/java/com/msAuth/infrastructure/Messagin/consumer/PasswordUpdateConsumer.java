package com.msAuth.infrastructure.Messagin.consumer;


import com.msAuth.application.Event.PasswordUpdateEvent;
import com.msAuth.application.exception.UserNotFoundException;
import com.msAuth.application.port.out.UserRepositoryPort;
import com.msAuth.domain.Model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordUpdateConsumer {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @RabbitListener(queues = "${app.rabbitmq.queue.password-update}")
    public void handlePasswordUpdate(PasswordUpdateEvent event) {
        if (event.userId() == null || event.newPassword() == null) {
            log.warn("Evento de actualización de password inválido, ignorando.");
            return;
        }

        log.info("Actualizando password para userId: {}", event.userId());

        User user = userRepositoryPort.findById(event.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + event.userId()));

        User updated = new User(
                user.getId(),
                user.getUserName(),
                passwordEncoder.encode(event.newPassword()),
                user.getEmail(),
                user.getUserType(),
                user.isUserStatus()
        );

        userRepositoryPort.save(updated);
        log.info("Password actualizado para userId: {}", event.userId());
    }
}