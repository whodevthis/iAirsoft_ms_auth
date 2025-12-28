package com.msAuth.application.port.in;

import com.msAuth.application.DTO.UserDTO;
import com.msAuth.domain.Model.RoleUser;

public interface CreateUserUseCase {
    UserDTO createUser(String userName, RoleUser role ,  String ip );
}
