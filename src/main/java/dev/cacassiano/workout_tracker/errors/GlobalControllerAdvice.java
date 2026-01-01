package dev.cacassiano.workout_tracker.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.cacassiano.workout_tracker.controllers.AuthController;
import dev.cacassiano.workout_tracker.controllers.WorkoutController;
import dev.cacassiano.workout_tracker.errors.custom.ErrorResponse;
import dev.cacassiano.workout_tracker.errors.custom.InvalidRequestResponse;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestControllerAdvice(assignableTypes = {AuthController.class, WorkoutController.class})
public class GlobalControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ApiResponse(
        responseCode = "404",
        description = "resource not found on db", 
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ErrorResponse notFoundExceptionHandler(NotFoundException ex){
        return new ErrorResponse(ex, 404);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ApiResponse(
        description = "invalid request content", 
        responseCode = "400",
        content = @Content(schema = @Schema(implementation = InvalidRequestResponse.class))
    )
    public InvalidRequestResponse invalidRequestArgumentsHandler(MethodArgumentNotValidException ex){
        
        return new InvalidRequestResponse(ex, 400);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
        responseCode = "500", 
        description = "something went wrong in the server",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ErrorResponse genericHandler(Exception ex){
        return new ErrorResponse(ex, 500);
    }
}
