package com.msAuth.application.port.in.access;

public interface LogInUseCase {
    String loginAndCreateToken(String username, String password, String ip);
}
