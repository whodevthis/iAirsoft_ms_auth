package com.msAuth.domain.Model;

public record Token ( Long id,
Long userId,
TokenType tokenType,
long dataLife){
}
