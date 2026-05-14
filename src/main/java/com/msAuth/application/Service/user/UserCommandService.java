package com.msAuth.application.Service.user;

import com.msAuth.application.dto.user.InputUserDto;
import com.msAuth.application.dto.user.UserDetailsDTO;
import com.msAuth.application.Event.NotificationEvent;
import com.msAuth.application.Event.UserAction;
import com.msAuth.application.Event.UserEvent;
import com.msAuth.application.exception.UserNotFoundException;
import com.msAuth.application.port.in.user.command.CreateUserUseCase;
import com.msAuth.application.port.in.user.command.DeleteUserUseCase;
import com.msAuth.application.port.in.user.command.UpdateUserUseCase;
import com.msAuth.application.port.out.UserRepositoryPort;
import com.msAuth.application.utils.GenericUtils;
import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Messagin.Producer.NotificationEventProducer;
import com.msAuth.infrastructure.Messagin.Producer.UserCreatedEventProducer;
import com.msAuth.infrastructure.Messagin.Producer.UserDeletedEventProducer;
import com.msAuth.infrastructure.Messagin.Producer.UserUpdatedEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandService implements CreateUserUseCase, DeleteUserUseCase, UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final GenericUtils genericUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserCreatedEventProducer userCreatedEventProducer;
    private final NotificationEventProducer notificationEventProducer;
    private final UserUpdatedEventProducer userUpdatedEventProducer;
    private final UserDeletedEventProducer userDeletedEventProducer;

    @Transactional
    @Override
    public UUID create(InputUserDto inputUserDto, String ip) {
        if (userRepositoryPort.existsByUserName(inputUserDto.userName())) {
            throw new RuntimeException("Username already exists: " + inputUserDto.userName());
        }

        User user = new User(null, inputUserDto.userName(), passwordEncoder.encode(inputUserDto.password()), // ✅ password encodeado
                inputUserDto.email(), inputUserDto.userType(), true);

        User saved = userRepositoryPort.save(user);

        if (inputUserDto.userType().name().equals("USER")) {
            userCreatedEventProducer.send(new UserEvent(saved.getId(), saved.getUserName(), null, ip,
                    System.currentTimeMillis(), true, UserAction.CREATED));
        }
        notificationEventProducer.send(new NotificationEvent(
                saved.getId(),
                saved.getEmail(),
                saved.getUserName()
        ));
        return saved.getId();
    }

    @Transactional
    @Override
    public UUID update(UserDetailsDTO userDetailsDTO, String ip) {
        User oldUser = userRepositoryPort.findById(userDetailsDTO.id()).orElseThrow(() -> new UserNotFoundException("User not found: " + userDetailsDTO.id()));

        User updated = new User(oldUser.getId(), genericUtils.applyIfChanged(oldUser.getUserName(), userDetailsDTO.userName()), genericUtils.applyIfChanged(oldUser.getPassword(), userDetailsDTO.password()), genericUtils.applyIfChanged(oldUser.getEmail(), userDetailsDTO.email()), genericUtils.applyIfChanged(oldUser.getUserType(), userDetailsDTO.userType()), genericUtils.applyIfChanged(oldUser.isUserStatus(), userDetailsDTO.userStatus()));

        User saved = userRepositoryPort.save(updated);

        userUpdatedEventProducer.send(new UserEvent(saved.getId(), saved.getUserName(),
                null, ip, System.currentTimeMillis(), userDetailsDTO.userStatus(), UserAction.UPDATED));

        return saved.getId();
    }

    @Transactional
    @Override
    public void delete(UUID id, String ip) {
        User user = userRepositoryPort.findById(id).orElseThrow(() -> new UserNotFoundException("User not found: " + id));

        userRepositoryPort.deleteById(id);

        userDeletedEventProducer.send(new UserEvent(user.getId(), user.getUserName(), null, ip, System.currentTimeMillis(),
                false, UserAction.DELETED));
    }
}