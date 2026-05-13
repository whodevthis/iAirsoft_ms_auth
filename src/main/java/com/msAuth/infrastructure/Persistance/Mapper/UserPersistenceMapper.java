package com.msAuth.infrastructure.Persistance.Mapper;

import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserEntity toEntity(User domain);
    User toDomain(UserEntity entity);
}