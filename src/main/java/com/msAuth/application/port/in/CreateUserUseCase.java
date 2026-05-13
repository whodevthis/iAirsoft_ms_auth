package com.msAuth.application.port.in;

import com.msAuth.application.DTO.UserDTO;
import com.msAuth.application.DTO.user.InputUserDto;
import com.msAuth.domain.Model.RoleUser;

import java.util.UUID;

public interface CreateUserUseCase {
    UUID create (InputUserDto inputUserDto);
}
