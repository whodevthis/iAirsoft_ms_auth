package com.msAuth.application.Event;

import com.msAuth.domain.Model.RoleUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private UUID id;
    private String userName;
    private RoleUser role;
    private String ip;
    private long timestamp; // para auditoría
    private UserStatus userStatus;
    private UserAction action; // nueva propiedad
}
