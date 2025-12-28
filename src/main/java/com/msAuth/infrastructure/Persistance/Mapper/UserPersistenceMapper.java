package com.msAuth.infrastructure.Persistance.Mapper;

import com.msAuth.application.DTO.Client.UserInternalDTO;
import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;

import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserEntity toEntity(User domain);
    User toDomain(UserEntity entity);
    UserInternalDTO toInternalDTO(UserEntity userEntity);

    User toDomain(Optional<UserEntity> userEntity);
}