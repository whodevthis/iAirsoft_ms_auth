package com.msAuth.application.Service.access;

import com.msAuth.application.Event.ExceptionAction;
import com.msAuth.application.Event.ExceptionEvent;
import com.msAuth.application.Event.UserAction;
import com.msAuth.application.Event.UserEvent;
import com.msAuth.application.exception.InvalidPasswordException;
import com.msAuth.application.exception.UserDisabledException;
import com.msAuth.application.exception.UserNotFoundException;
import com.msAuth.application.port.in.access.LogInUseCase;
import com.msAuth.application.port.out.UserRepositoryPort;
import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Messagin.Producer.ExceptionEventProducer;
import com.msAuth.infrastructure.Messagin.Producer.UserLoggedInEventProducer;
import com.msAuth.infrastructure.Security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogInUseCaseImpl implements LogInUseCase {

    private final JwtProvider jwtProvider;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final UserLoggedInEventProducer userLoggedInEventProducer;
    private final ExceptionEventProducer exceptionEventProducer;

    @Override
    public String loginAndCreateToken(String username, String password, String ip) {
        try {
            User user = validateUser(username, password);

            String token = jwtProvider.createToken(user.getId(), user.getUserType().name()); // ✅ UUID

            userLoggedInEventProducer.send(new UserEvent(
                    user.getId(),
                    user.getUserName(),
                    null,
                    ip,
                    System.currentTimeMillis(),
                    true,
                    UserAction.LOGIN
            ));

            return token;

        } catch (RuntimeException e) {
            ExceptionAction action;
            if (e instanceof UserNotFoundException)    action = ExceptionAction.USER_NOT_FOUND;
            else if (e instanceof InvalidPasswordException) action = ExceptionAction.INVALID_PASSWORD;
            else if (e instanceof UserDisabledException)    action = ExceptionAction.USER_DISABLED;
            else                                            action = ExceptionAction.ERROR_CONNECTION;

            exceptionEventProducer.send(
                    new ExceptionEvent(username, action, ip, System.currentTimeMillis())
            );
            throw e;
        }
    }

    private User validateUser(String username, String password) {
        if (username == null || username.isBlank()) throw new RuntimeException("Nombre de usuario vacío");
        if (password == null || password.isBlank()) throw new RuntimeException("Contraseña vacía");

        User user = userRepositoryPort.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + username));

        if (!user.isUserStatus())
            throw new UserDisabledException("Usuario deshabilitado: " + username);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidPasswordException("Contraseña incorrecta");

        return user;
    }
}