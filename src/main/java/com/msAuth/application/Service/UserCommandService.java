package com.msAuth.application.Service;

import com.msAuth.application.DTO.user.InputUserDto;
import com.msAuth.application.DTO.user.UserDetailsDTO;
import com.msAuth.application.port.in.CreateUserUseCase;
import com.msAuth.application.port.in.DeleteUserUseCase;
import com.msAuth.application.port.in.UpdateUserUseCase;
import com.msAuth.application.port.out.UserRepositoryPort;

import com.msAuth.application.utils.GenericUtils;
import com.msAuth.domain.Model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandService implements CreateUserUseCase, DeleteUserUseCase, UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final GenericUtils genericUtils;

    @Transactional
    @Override
    public UUID create(InputUserDto inputUserDto) {
        User user = new User(null, inputUserDto.userName(), inputUserDto.password(), inputUserDto.email(), inputUserDto.userType(), true

        );
        return userRepositoryPort.save(user).getId();
    }


    @Transactional
    @Override
    public UUID update(UserDetailsDTO userDetailsDTO) {
        User oldUser = userRepositoryPort.findById(userDetailsDTO.id()).orElseThrow(() -> new EntityNotFoundException("User to update not found "));

        User user = new User(oldUser.getId(), genericUtils.applyIfChanged(oldUser.getUserName(), userDetailsDTO.userName()), genericUtils.applyIfChanged(oldUser.getPassword(), userDetailsDTO.password()), genericUtils.applyIfChanged(oldUser.getEmail(), userDetailsDTO.email()), genericUtils.applyIfChanged(oldUser.getUserType(), userDetailsDTO.userType()), genericUtils.applyIfChanged(oldUser.isUserStatus(), userDetailsDTO.userStatus()));

        return userRepositoryPort.save(user).getId();
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        userRepositoryPort.findById(id).orElseThrow(() -> new EntityNotFoundException("User to update not found"));
        userRepositoryPort.deleteById(id);
    }


}
