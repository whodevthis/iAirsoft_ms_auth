package com.msAuth.infrastructure.Controller;

import com.msAuth.application.DTO.UserDTO;
import com.msAuth.application.Mapper.UserMapper;
import com.msAuth.application.port.in.CreateUserUseCase;
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
    private final UserMapper userMapper;

    @Operation(summary = "Login de usuario", description = "Valida credenciales y genera un JWT")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {

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



}