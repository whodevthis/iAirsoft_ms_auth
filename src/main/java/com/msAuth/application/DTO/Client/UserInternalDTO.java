package com.msAuth.application.DTO.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInternalDTO {
    private UUID userId;
    private String userName;
    private String ip;
    private String userStatus;
}


