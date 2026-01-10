package dev.cacassiano.workout_tracker.DTOs.exercises;

import java.util.Set;

import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReferenceDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ExerciseReqDTO {
    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotNull(message = "reps cannot be null")
    @Min(value = 1, message = "reps min value is 1")
    private Integer reps;

    @NotNull(message = "series cannot be null")
    @Min(value = 1, message = "series min value is 1")
    private Integer series;

    @NotBlank(message = "category cannot be blank")
    private String category;

    Set<WorkoutReferenceDTO> workouts;
}
