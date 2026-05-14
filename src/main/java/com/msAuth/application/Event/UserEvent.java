package com.msAuth.application.Event;

import com.msAuth.domain.types.UserType;
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
    private UserType userType;
    private String ip;
    private long timestamp;
    private boolean userStatus;
    private UserAction action;
}