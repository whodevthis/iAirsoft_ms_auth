package com.msAuth.infrastructure.Controller;

import com.msAuth.application.DTO.UserDTO;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.port.in.CreateUserUseCase;
import com.msAuth.application.port.in.DeleteUserUseCase;
import com.msAuth.application.port.in.LogInUseCase;
import com.msAuth.application.port.in.UpdateUserUseCase;
import com.msAuth.domain.Model.RoleUser;
import com.msAuth.infrastructure.Security.JwtProvider; // Asegúrate de que el nombre sea correcto
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Access Module", description = "Endpoints para Login y Auditoría") // Agrupa tus endpoints
public class AuthController {
    private final CreateUserUseCase createUserUseCase;
    private final LogInUseCase logInUseCase;
    private final JwtProvider jwtProvider;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserMapper userMapper;

    @Operation(summary = "Login de usuario", description = "Valida credenciales y genera un JWT")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping("/login/{username}/{password}")
    public ResponseEntity<?> login(@PathVariable String username, @PathVariable String password, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String token = logInUseCase.loginAndCreateToken(username, password, ip);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create User on System", description = "Create user is success and access into system")
    @ApiResponse(responseCode = "200", description = "Creation Success")
    @ApiResponse(responseCode = "401", description = "Error on creation user ")
    @PostMapping("/createUser/{username}/{role}")
    public ResponseEntity<?> createUser(
            @PathVariable String username,
            @PathVariable RoleUser role,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        UserDTO createdUser = createUserUseCase.createUser(username, role, ip);

        String token = logInUseCase.loginAndCreateToken(username, createdUser.getPassword(), ip);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Update User on System", description = "Update user ons system")
    @ApiResponse(responseCode = "200", description = "Update Success")
    @ApiResponse(responseCode = "401", description = "Error on Update user ")
    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserDTO userRequest,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();

        UserDTO updatedUser = updateUserUseCase.updateUser(userRequest, ip);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "SoftDelete User on System", description = "SoftDelete user ons system, consist in change his UserStatus to DEACTIVATE")
    @ApiResponse(responseCode = "204", description = "User deactivated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/softDelete/{userId}")
    public void softDeleteUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        deleteUserUseCase.softDeleteUser(userId,ip);


    }

    @Operation(summary = "Delete User on System", description = "Delete user on system when the creation date user is mayor to 5 years and 1 day")
    @ApiResponse(responseCode = "204", description = "User Delete successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/delete/{userId}")
    public void deleteUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        deleteUserUseCase.deleteUser(userId,ip);


    }

    @Operation(summary = "Rigth to be Forbidden Delete User on System",
            description = "Delete User on system on user peticion")
    @ApiResponse(responseCode = "204", description = "Rigth to be Forbidden Delete  successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/rtbfDelete/{userId}")
    public void rtbfDelete(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        deleteUserUseCase.rtbfUser(userId,ip);


    }



}