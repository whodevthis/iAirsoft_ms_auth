package com.msAuth.application.port.in.user.command;

import com.msAuth.application.dto.user.InputUserDto;

import java.util.UUID;

public interface CreateUserUseCase {
    UUID create (InputUserDto inputUserDto, String ip);
}
