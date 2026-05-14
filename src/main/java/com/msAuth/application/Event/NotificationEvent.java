package com.msAuth.application.Event;

import java.util.UUID;

public record NotificationEvent(
        UUID userId,
        String email,
        String userName
) {}