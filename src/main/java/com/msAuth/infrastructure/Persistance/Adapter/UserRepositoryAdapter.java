package com.msAuth.infrastructure.Persistance.Adapter;

import com.msAuth.application.DTO.Client.UserInternalDTO;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.port.out.UserRepositoryPort;
import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import com.msAuth.infrastructure.Persistance.JPARepository.JpaUserRepository;
import com.msAuth.infrastructure.Persistance.Mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;

    private void sendAuditEvent(UserEntity userEntity, String ip, String routingKey) {
        UserInternalDTO internalDTO = new UserInternalDTO(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getRole().name(),
                ip,
                userEntity.getUserStatus().name()
        );
        rabbitTemplate.convertAndSend("ms.auditoria.exchange", routingKey, internalDTO);
    }

    @Override
    public User createUser(User user, String ip) {


        UserEntity userEntity=userPersistenceMapper.toEntity(user);

        UserEntity userSaved=jpaUserRepository.save(userEntity);

        UserInternalDTO internalDTO = new UserInternalDTO(
                userSaved.getId(),
                userSaved.getUserName(),
                userSaved.getRole().name(),
                ip,
                userSaved.getUserStatus().name()
        );

        sendAuditEvent(userSaved, ip, "user.created");

        return userPersistenceMapper.toDomain(userSaved);
    }

    @Override
    public User updateUser(User user, String ip) {

        UserEntity userEntity=userPersistenceMapper.toEntity(user);
        UserEntity userSaved = jpaUserRepository.save(userEntity);

        UserInternalDTO internalDTO = new UserInternalDTO(
                userSaved.getId(),
                userSaved.getUserName(),
                userSaved.getRole().name(),
                ip,
                userSaved.getUserStatus().name()
        );
        sendAuditEvent(userSaved, ip, "user.updated");

        return userPersistenceMapper.toDomain(userSaved);
    }



    @Override
    public User deleteUser(Long userId, String ip) {
        return null;
    }

    @Override
    public User rtbfUser(Long userId, String ip) {
        return null;
    }

    @Override
    public User desactivateUser(Long userId, String ip) {
        return null;
    }



}