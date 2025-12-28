package com.msAuth.application.Service;

import com.msAuth.application.DTO.UserDTO;
import com.msAuth.application.Event.UserAction;
import com.msAuth.application.Event.UserEvent;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.port.in.CreateUserUseCase;
import com.msAuth.domain.Model.RoleUser;
import com.msAuth.domain.Model.UserStatus;
import com.msAuth.infrastructure.Messagin.Producer.UserCreatedEventProducer;
import com.msAuth.infrastructure.Persistance.Entity.UserEntity;
import com.msAuth.infrastructure.Persistance.JPARepository.JpaUserRepository;
import com.msAuth.infrastructure.Persistance.Mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import com.msAuth.domain.Model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserPersistenceMapper userPersistenceMapper;
    private final UserCreatedEventProducer userCreatedEventProducer;
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final List<Character> CODE = List.of(
            'q','a','z','x','s','w','e','d','c','v',
            'f','r','m','l','p','o','i','j','n','h',
            'u','y','t','4','5','6','7','8','3','9','2','1','0'
    );

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            char c = CODE.get(RANDOM.nextInt(CODE.size()));
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public UserDTO createUser(String userName,  RoleUser role , String ip ) {

        String password=generatePassword();

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity(
            null,
                userName,
                encodedPassword,
                role,
                UserStatus.ACTIVE,
                System.currentTimeMillis()
        );

        UserEntity userSaved=  jpaUserRepository.save(userEntity);

        User user =  userPersistenceMapper.toDomain(userSaved);

        UserEvent userEvent = new UserEvent(
                user.userName(),user.role(),ip,  System.currentTimeMillis(),user.userStatus(),UserAction.CREATED

        );

         userCreatedEventProducer.send(userEvent);

    return userMapper.toDto(user);
    }
}