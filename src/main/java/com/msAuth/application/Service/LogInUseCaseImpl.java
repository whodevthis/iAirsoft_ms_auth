package com.msAuth.application.Service;

import com.msAuth.application.DTO.Client.UserInternalDTO;
import com.msAuth.application.Event.ExceptionAction;
import com.msAuth.application.Event.ExceptionEvent;
import com.msAuth.application.Event.UserAction;
import com.msAuth.application.Event.UserEvent;
import com.msAuth.application.exception.InvalidPasswordException;
import com.msAuth.application.exception.UserDisabledException;
import com.msAuth.application.exception.UserNotFoundException;
import com.msAuth.application.port.in.LogInUseCase;
import com.msAuth.domain.Model.RoleUser;
import com.msAuth.infrastructure.Messagin.Producer.ExceptionEventProducer;
import com.msAuth.infrastructure.Messagin.Producer.UserLoggedInEventProducer;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import com.msAuth.infrastructure.Persistance.JPARepository.JpaUserRepository;
import com.msAuth.infrastructure.Persistance.Mapper.UserPersistenceMapper;
import com.msAuth.infrastructure.Security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogInUseCaseImpl implements LogInUseCase {

    private final JwtProvider jwtProvider;
    private final UserLoggedInEventProducer userLoggedInEventProducer;
    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ExceptionEventProducer exceptionEventProducer;
    @Override
    public String loginAndCreateToken(String username, String password, String ip) {

        try {
            UserInternalDTO user =      validateUser(username, password, ip);
            String token = jwtProvider.createToken(user.getUserId(),user.getRole());

            UserEvent event = new UserEvent(
                    user.getUserName(),
                    RoleUser.valueOf(user.getRole()),
                    ip,
                    System.currentTimeMillis(),
                    UserStatus.valueOf(user.getUserStatus()),
                    UserAction.LOGIN
            );
            userLoggedInEventProducer.send(event);

            return token;

        } catch (RuntimeException e) {
            ExceptionAction action;

            if (e instanceof UserNotFoundException) action = ExceptionAction.USER_NOT_FOUND;
            else if (e instanceof InvalidPasswordException) action = ExceptionAction.INVALID_PASSWORD;
            else if (e instanceof UserDisabledException) action = ExceptionAction.USER_DISABLED;
            else action = ExceptionAction.ERROR_CONNECTION;

            ExceptionEvent event = new ExceptionEvent(username, action, ip, System.currentTimeMillis());

            exceptionEventProducer.send(event);

            throw e;
        }
    }


    private UserInternalDTO validateUser(String username, String password, String ip) {

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Nombre de usuario vacío");
        }

        if (password == null || password.isBlank()) {
            throw new RuntimeException("Constraseña vacía");
        }



        UserEntity userEntity = jpaUserRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));


        if (!userEntity.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new UserDisabledException("Usuario deshabilitado");
        }

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new InvalidPasswordException("Contraseña incorrecta");
        }

        UserInternalDTO userInternalDTO = userMapper.toInternalDTO(userEntity);

        userInternalDTO.setIp(ip);

        return userInternalDTO;
    }
}


