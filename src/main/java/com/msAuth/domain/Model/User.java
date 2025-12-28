package com.msAuth.domain.Model;



public record User(
        Long id,
        String userName,
        String password,
        RoleUser role,
        UserStatus userStatus,
        long creationDate
) {

}
