package com.msAuth.application.dto.user;

import com.msAuth.domain.types.UserType;


import java.time.LocalDateTime;
import java.util.UUID;

public record UserDetailsDTO (
    UUID id,
    String userName,
    String password,
    String email,
    UserType userType,
    boolean userStatus,
    LocalDateTime createdAt
    )
    {

}
