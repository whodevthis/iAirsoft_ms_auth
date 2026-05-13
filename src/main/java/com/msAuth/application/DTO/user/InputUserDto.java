package com.msAuth.application.DTO.user;

import com.msAuth.domain.types.UserType;

public record InputUserDto(String userName, String password, String email, UserType userType) {

}
