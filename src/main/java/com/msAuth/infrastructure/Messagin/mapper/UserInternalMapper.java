package com.msAuth.infrastructure.Messagin.mapper;

import com.msAuth.application.DTO.Client.UserInternalDTO;
import com.msAuth.domain.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInternalMapper {
    User toDomain(UserInternalDTO dto);
}
