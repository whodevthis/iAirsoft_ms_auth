package com.msAuth.application.Mapper;

import com.msAuth.application.DTO.UserDTO;

import com.msAuth.domain.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto (User domain);
    User toDomain (UserDTO dto);
   // User toDomain (UserRequest userRequest);
    }
