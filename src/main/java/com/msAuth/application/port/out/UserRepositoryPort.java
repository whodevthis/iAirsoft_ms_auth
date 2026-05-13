package com.msAuth.application.port.out;

import com.msAuth.application.DTO.user.UserDto;
import com.msAuth.domain.Model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByUserName(String userName);
    Optional<User> findById(UUID id);
    void deleteById(UUID id);
    List<User> search(Specification<User> spec);
    boolean existsByUserName(String userName);
    List<User> findAll();
}