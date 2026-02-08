package dev.cacassiano.workout_tracker.errors.custom;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;


import lombok.Getter;

@Getter
public class InvalidRequestResponse extends ErrorResponse{

    private List<String> errors;

    public InvalidRequestResponse(MethodArgumentNotValidException ex) {
        super("invalid request content", 400);
        this.errors = ex.getFieldErrors()
            .stream()
            .map(e -> "invalid '"+e.getField() + "' field: " + e.getDefaultMessage())
            .toList();
    }

    public InvalidRequestResponse(HttpMessageNotReadableException ex) {
        super("invalid request content", 400);
        String problem = ex.getMessage().split("problem: ")[1];
        this.errors = Arrays.asList(problem);
    }
    
}
