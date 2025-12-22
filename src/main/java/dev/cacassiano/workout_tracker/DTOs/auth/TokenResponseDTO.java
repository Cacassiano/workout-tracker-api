package dev.cacassiano.workout_tracker.DTOs.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDTO {
    private String token;
}
