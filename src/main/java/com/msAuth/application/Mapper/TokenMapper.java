package com.msAuth.application.Mapper;

import com.msAuth.application.DTO.TokenDTO;
import com.msAuth.domain.Model.Token;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface TokenMapper {
    TokenDTO toDto (Token domain);
    Token toDomain (TokenDTO dto);
}
