package com.msAuth.application.port.in.user.command;

import java.util.UUID;

public interface DeleteUserUseCase {
    void delete (UUID id, String ip);
}
