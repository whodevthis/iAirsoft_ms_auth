package com.msAuth.application.DTO.user;

import com.msAuth.domain.states.UserType;

public record InputUserDto(String userName, String password, String email, UserType userType) {

}
