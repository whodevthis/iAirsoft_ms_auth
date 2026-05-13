package com.msAuth.application.port.out;

import com.msAuth.domain.Model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByUserName(String userName);
    Optional<User> findById(UUID id);
    void deleteById(UUID id);
    boolean existsByUserName(String userName);
}