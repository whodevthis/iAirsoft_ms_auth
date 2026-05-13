package com.msAuth.application.DTO;

import com.msAuth.domain.states.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private Long id;
    private  Long userId;
    private  TokenType tokenType;
    private  long dataLife;
}
