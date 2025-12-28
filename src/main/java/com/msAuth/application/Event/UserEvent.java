package com.msAuth.application.Event;

import com.msAuth.domain.Model.RoleUser;
import com.msAuth.domain.Model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String userName;
    private RoleUser role;
    private String ip;
    private long timestamp; // para auditoría
    private UserStatus userStatus;
    private UserAction action; // nueva propiedad
}
