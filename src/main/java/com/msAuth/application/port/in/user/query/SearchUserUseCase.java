package com.msAuth.application.port.in.user.query;

import com.msAuth.application.DTO.user.UserDto;

import java.util.List;

public interface SearchUserUseCase {

        List<UserDto> search(String data);


}
