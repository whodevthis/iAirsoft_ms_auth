package com.msAuth.application.port.out;

import com.msAuth.domain.Model.User;


public interface UserRepositoryPort {
    User createUser(User user, String ip);
    User deleteUser (Long userId, String ip);
    User rtbfUser(Long userId, String ip);
    User desactivateUser (Long userId, String ip);
    User updateUser(User user, String ip);

}
