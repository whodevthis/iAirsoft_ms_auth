package com.msAuth.application.port.in;

import com.msAuth.application.DTO.UserDTO;

public interface UpdateUserUseCase {
    UserDTO updateUser(UserDTO userDTO,String ip);
}
