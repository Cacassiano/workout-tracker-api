package dev.cacassiano.workout_tracker.errors.custom;

import java.util.List;

import org.springframework.web.bind.MethodArgumentNotValidException;

import lombok.Getter;

@Getter
public class InvalidRequestResponse extends ErrorResponse{

    private List<String> errors;

    public InvalidRequestResponse(MethodArgumentNotValidException ex, int status) {
        super("invalid request content", status);
        this.errors = ex.getFieldErrors()
            .stream()
            .map(e -> "invalid '"+e.getField() + "' field: " + e.getDefaultMessage())
            .toList();
    }
    
}
