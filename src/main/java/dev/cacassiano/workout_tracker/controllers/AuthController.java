package dev.cacassiano.workout_tracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.auth.TokenDTO;
import dev.cacassiano.workout_tracker.DTOs.auth.UserRequestDTO;
import dev.cacassiano.workout_tracker.use_cases.services.auth.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService authService;
    @PostMapping("/register")
    @ApiResponse(description = "Register a new User", responseCode = "201")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO request) throws MethodArgumentNotValidException{
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @ApiResponse(description = "Login succesfully and return the token", responseCode = "200")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid UserRequestDTO request) throws MethodArgumentNotValidException{
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new TokenDTO(token));
    }
}