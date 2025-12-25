package dev.cacassiano.workout_tracker.errors;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {




    @ExceptionHandler(Exception.class)
    public String genericHandler(Exception ex){
        return ex.getMessage();
    }
}
