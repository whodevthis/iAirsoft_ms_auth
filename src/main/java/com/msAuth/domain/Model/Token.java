package com.msAuth.domain.Model;

import com.msAuth.domain.states.TokenType;
import com.msAuth.domain.types.TokenType;

public record Token (Long id,
                     Long userId,
                     TokenType tokenType,
                     long dataLife){
}
