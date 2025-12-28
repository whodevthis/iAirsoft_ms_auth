package com.msAuth.infrastructure.Persistance.JPARepository;

import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String username);


}