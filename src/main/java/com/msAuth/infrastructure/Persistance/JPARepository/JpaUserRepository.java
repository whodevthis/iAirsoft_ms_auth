package com.msAuth.infrastructure.Persistance.JPARepository;

import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUserName(String userName);
    boolean existsByUserName(String userName);
}