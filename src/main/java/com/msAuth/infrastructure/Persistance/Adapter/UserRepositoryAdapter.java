package com.msAuth.infrastructure.Persistance.Adapter;

import com.msAuth.application.port.out.UserRepositoryPort;
import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import com.msAuth.infrastructure.Persistance.JPARepository.JpaUserRepository;
import com.msAuth.infrastructure.Persistance.Mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public User save(User user) {
        return userPersistenceMapper.toDomain(jpaUserRepository.save(userPersistenceMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return jpaUserRepository.findByUserName(userName).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return jpaUserRepository.existsByUserName(userName);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream().map(userPersistenceMapper::toDomain).toList();
    }

    @Override
    public List<User> search(Specification<User> spec) {
        Specification<UserEntity> entitySpec = (root, query, cb) -> spec.toPredicate((Root) root, query, cb);
        return jpaUserRepository.findAll(entitySpec)
                .stream().map(userPersistenceMapper::toDomain).toList();
    }
}