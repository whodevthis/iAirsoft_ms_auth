package com.msAuth.application.port.in;


import com.msAuth.application.DTO.user.UserDetailsDTO;

import java.util.UUID;

public interface UpdateUserUseCase {
    UUID update (UserDetailsDTO userDetailsDTO);
}
