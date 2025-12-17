package dev.cacassiano.workout_tracker.DTOs;

public record ResponseDTO<T>(
    T data
){
}
