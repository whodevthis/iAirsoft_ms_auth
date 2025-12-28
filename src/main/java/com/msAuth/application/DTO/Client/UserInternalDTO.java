package com.msAuth.application.DTO.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInternalDTO {
    private Long userId;
    private String userName;
    private String role;
    private String ip;
    private String userStatus;
}


