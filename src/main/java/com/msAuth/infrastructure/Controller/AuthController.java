package com.msAuth.infrastructure.Controller;

import com.msAuth.application.dto.user.InputUserDto;
import com.msAuth.application.dto.user.UserDetailsDTO;
import com.msAuth.application.dto.user.UserDto;
import com.msAuth.application.Service.user.UserCommandService;
import com.msAuth.application.Service.user.UserQueryService;
import com.msAuth.application.port.in.access.LogInUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and user management")
public class AuthController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final LogInUseCase logInUseCase;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> credentials,  // ✅ no más PathVariable
            HttpServletRequest request) {

        String token = logInUseCase.loginAndCreateToken(
                credentials.get("userName"),
                credentials.get("password"),
                request.getRemoteAddr()
        );
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ── COMMAND ─────────────────────────────────────────────
    @Operation(summary = "Create user")
    @PostMapping("/users")
    public ResponseEntity<UUID> create(
            @RequestBody @Valid InputUserDto dto,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userCommandService.create(dto, request.getRemoteAddr()));
    }

    @Operation(summary = "Update user")
    @PutMapping("/users")
    public ResponseEntity<UUID> update(
            @RequestBody @Valid UserDetailsDTO dto,
            HttpServletRequest request) {
        return ResponseEntity.ok(userCommandService.update(dto, request.getRemoteAddr()));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            HttpServletRequest request) {
        userCommandService.delete(id, request.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    // ── QUERY ────────────────────────────────────────────────

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userQueryService.findAll());
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailsDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userQueryService.findById(id));
    }
}