package com.msAuth.application.Event;

import java.util.UUID;

public record PasswordUpdateEvent(
        UUID userId,
        String newPassword
) {}