package com.msAuth.application.DTO.user;

import com.msAuth.domain.states.UserType;


import java.util.UUID;

public record UserDetailsDTO (
    UUID id,
    String userName,
    String password,
    String email,
    UserType userType,
    boolean userStatus)
    {

}
