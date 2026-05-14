package com.msAuth.application.port.in.user.query;

import com.msAuth.application.dto.user.UserDto;

import java.util.List;

public interface SearchUserUseCase {

        List<UserDto> search(String data);


}
