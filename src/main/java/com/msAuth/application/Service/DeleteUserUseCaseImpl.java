package com.msAuth.application.Service;

import com.msAuth.application.Event.UserAction;
import com.msAuth.application.Event.UserEvent;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.port.in.DeleteUserUseCase;
import com.msAuth.domain.Model.User;
import com.msAuth.domain.Model.UserStatus;
import com.msAuth.infrastructure.Messagin.Producer.UserDeletedEventProducer;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import com.msAuth.infrastructure.Persistance.JPARepository.JpaUserRepository;
import com.msAuth.infrastructure.Persistance.Mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {


    private final  UserMapper userMapper;
    private final UserPersistenceMapper userPersistenceMapper;
    private final JpaUserRepository jpaUserRepository;
    private final UserDeletedEventProducer userDeletedEventProducer;

    @Override
    @Transactional
    public void deleteUser(Long userId,String ip) {

        jpaUserRepository.findById(userId).ifPresent(userEntity -> {

        long creationDate= userEntity.getCreationDate();

        long timeToDelete= creationDate+157680086400L;

        if(System.currentTimeMillis()<timeToDelete){

            User user=userPersistenceMapper.toDomain(userEntity);

            UserEvent userEvent = new UserEvent(
                    user.userName(),
                    user.role(),
                    ip,
                    System.currentTimeMillis(),
                    UserStatus.INACTIVE,
                    UserAction.DELETED
            );

            userDeletedEventProducer.send(userEvent);

            jpaUserRepository.delete(userEntity);

        }

        });
     }

    @Override
    public void rtbfUser(Long userId, String ip) {
        jpaUserRepository.findById(userId).ifPresent(userEntity -> {

        User user=userPersistenceMapper.toDomain(userEntity);

            UserEvent userEvent = new UserEvent(
                    user.userName(),user.role(),ip,  System.currentTimeMillis(), UserStatus.INACTIVE, UserAction.DELETED

            );

            userDeletedEventProducer.send(userEvent);

            jpaUserRepository.delete(userEntity);

        });

    }

    @Override
    @Transactional
    public void softDeleteUser(Long userId, String ip) {

        jpaUserRepository.findById(userId).ifPresent(userEntity -> {

        UserEntity newEntity =new UserEntity(

                userEntity.getId(),

                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                UserStatus.INACTIVE,
                userEntity.getCreationDate()
        );
            jpaUserRepository.save(newEntity);
            User user = userPersistenceMapper.toDomain(newEntity);
            UserEvent userEvent = new UserEvent(
                    user.userName(),
                    user.role(),
                    ip,
                    System.currentTimeMillis(),
                    UserStatus.INACTIVE,
                    UserAction.DELETED
            );

            userDeletedEventProducer.send(userEvent);
        });
    }
}
