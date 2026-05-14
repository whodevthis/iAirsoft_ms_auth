package com.msAuth.application.dto.user;

import com.msAuth.domain.types.UserType;


import java.util.UUID;

public record UserDto(UUID id, String userName) {

}
