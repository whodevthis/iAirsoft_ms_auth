package com.msAuth.application.Mapper;

import com.msAuth.application.dto.user.UserDetailsDTO;
import com.msAuth.application.dto.user.UserDto;
import com.msAuth.application.dto.user.InputUserDto;
import com.msAuth.domain.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User domain);
    UserDetailsDTO toDetailsDto(User domain);
    User toDomain(InputUserDto dto);
}