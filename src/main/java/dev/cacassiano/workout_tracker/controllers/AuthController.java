package dev.cacassiano.workout_tracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.auth.TokenResponseDTO;
import dev.cacassiano.workout_tracker.DTOs.auth.UserRequestDTO;
import dev.cacassiano.workout_tracker.services.auth.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService authService;
    @PostMapping("/register")
    @ApiResponses(value = {
        @ApiResponse(description = "Register a new User", responseCode = "201"),
        @ApiResponse(description = "Invalid request body", responseCode = "400")
    })
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid UserRequestDTO request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
