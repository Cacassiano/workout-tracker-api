package dev.cacassiano.workout_tracker.errors.custom;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;

    public ErrorResponse(Exception ex, int status){
        this.message = ex.getMessage();
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
    
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}
