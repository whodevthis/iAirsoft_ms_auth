package com.msAuth.application.port.in;

public interface LogInUseCase {
    String loginAndCreateToken(String username, String password, String ip);
}
