package com.msAuth.application.port.in;

public interface DeleteUserUseCase {
    void deleteUser (Long userId,String ip);
    void rtbfUser(Long userId,String ip);
    void softDeleteUser(Long userId, String ip);
}
