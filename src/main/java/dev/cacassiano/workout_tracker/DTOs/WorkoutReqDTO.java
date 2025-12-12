package dev.cacassiano.workout_tracker.DTOs;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkoutReqDTO {
    
    @NotBlank
    private final String title;

    @NotBlank
    private final String schedule_type;

    @NotNull
    private final LocalDateTime scheduled_date;

    @NotNull
    private final Boolean completed;

    private final Set<ExerciseReqDTO> exercises;
}
