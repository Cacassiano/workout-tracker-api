package dev.cacassiano.workout_tracker.DTOs.auth;

import lombok.Getter;

@Getter
public class TokenDTO {
    private String token;

    public TokenDTO(String token) {
        if (token.contains("Bearer")) {
            token = token.substring(7);
        }
        this.token = token;
    }

}
