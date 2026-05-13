package com.msAuth.application.Service;

import com.msAuth.application.DTO.UserDTO;
import com.msAuth.application.Event.UserAction;
import com.msAuth.application.Event.UserEvent;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.port.in.UpdateUserUseCase;
import com.msAuth.domain.Model.RoleUser;
import com.msAuth.domain.Model.User;
import com.msAuth.infrastructure.Messagin.Producer.UserUpdatedEventProducer;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import com.msAuth.infrastructure.Persistance.JPARepository.JpaUserRepository;
import com.msAuth.infrastructure.Persistance.Mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserMapper userMapper;
    private final UserPersistenceMapper userPersistenceMapper;
    private final JpaUserRepository jpaUserRepository;
    private final UserUpdatedEventProducer userUpdatedEventProducer;

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, String ip) {

        UserEntity userEntity = jpaUserRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        String username;
        if (userDTO.getUserName() == null || userDTO.getUserName().isBlank() || userDTO.getUserName().equals(userEntity.getUserName())) {
            username = userEntity.getUserName();
        } else {
            username = userDTO.getUserName();   }


        String password;
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank() || userDTO.getPassword().equals(userEntity.getPassword())) {
            password = userEntity.getPassword();
        } else {
            password = userDTO.getPassword();
        }

        RoleUser roleUser;
        if (userDTO.getRole() == null || userDTO.getRole().equals(userEntity.getRole())) {
            roleUser = userEntity.getRole();
        } else {
            roleUser = userDTO.getRole();
        }

        UserStatus status;
        if (userDTO.getUserStatus() == null || userDTO.getUserStatus().equals(userEntity.getUserStatus())) {
            status = userEntity.getUserStatus();
        } else {
            status = userDTO.getUserStatus();
        }


        UserEntity newEntity=  new UserEntity(
                userEntity.getId(),
                    username,
                    password,
                    roleUser,
                    status,
                userEntity.getCreationDate()
            );
            jpaUserRepository.save(newEntity);

            User user=userPersistenceMapper.toDomain(newEntity);

            UserEvent userEvent = new UserEvent(
                    user.userName(),
                    user.role(),
                    ip,
                    System.currentTimeMillis(),
                    user.userStatus(),
                    UserAction.UPDATED
            );

            userUpdatedEventProducer.send(userEvent);

        return userMapper.toDto(user);
    }
}
