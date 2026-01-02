package dev.cacassiano.workout_tracker.DTOs.workouts;

import dev.cacassiano.workout_tracker.entities.Workout;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseSummaryDTO;

@Getter
@Setter
public class WorkoutResDTO extends WorkoutSummaryDTO{

    Set<ExerciseSummaryDTO> exercises;

    public WorkoutResDTO(Workout w) {
        super(w);
        this.exercises = ExerciseSummaryDTO.convertAll(w.getExercises());
    }
    
}
