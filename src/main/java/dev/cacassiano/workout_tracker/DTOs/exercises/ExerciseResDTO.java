package dev.cacassiano.workout_tracker.DTOs.exercises;

import java.util.Set;
import java.util.stream.Collectors;

import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutSummaryDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import lombok.Getter;

@Getter
public class ExerciseResDTO extends ExerciseSummaryDTO{

    Set<WorkoutSummaryDTO> workouts;

    public ExerciseResDTO(Exercise e) {
        super(e);
        this.workouts = e.getWorkouts().stream().map(WorkoutSummaryDTO::new).collect(Collectors.toSet()); 
    }
    
}
