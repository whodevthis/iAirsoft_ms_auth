package com.msAuth.application.Service.user;

import com.msAuth.application.dto.user.UserDetailsDTO;
import com.msAuth.application.dto.user.UserDto;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.exception.UserNotFoundException;
import com.msAuth.application.port.out.UserRepositoryPort;
import com.msAuth.application.utils.GenericUtils;
import com.msAuth.domain.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;
    private  final GenericUtils genericUtils;


    public UserDetailsDTO findById(UUID id) {
        return userMapper.toDetailsDto(
                userRepositoryPort.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found: " + id))
        );
    }

    public List<UserDto> findAll() {
        return userRepositoryPort.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserDto> search(String data) {
        List<User> users = userRepositoryPort.search(GenericUtils.search(data, User.class));
        return users.stream().map(userMapper::toDto).toList();
    }

}