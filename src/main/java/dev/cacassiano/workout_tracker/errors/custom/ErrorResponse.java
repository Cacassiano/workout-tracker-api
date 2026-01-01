package dev.cacassiano.workout_tracker.errors.custom;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String messsage;
    private LocalDateTime timestamp;

    public ErrorResponse(Exception ex){
        this.messsage = ex.getMessage();
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message) {
        this.messsage = message;
        this.timestamp = LocalDateTime.now();
    }
}
