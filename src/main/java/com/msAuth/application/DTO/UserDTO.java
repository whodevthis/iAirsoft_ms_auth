package com.msAuth.application.DTO;


import com.msAuth.domain.Model.RoleUser;
import com.msAuth.domain.Model.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull
    private Long id;
    @NotBlank
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private RoleUser role;
    @NotNull
    private UserStatus userStatus;
    private  long creationDate;


}
