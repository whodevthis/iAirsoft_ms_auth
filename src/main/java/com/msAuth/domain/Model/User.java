package com.msAuth.domain.Model;


import com.msAuth.domain.types.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String userName;
    private String password;
    private  String email;
    private  UserType userType;
    private  boolean userStatus;

}