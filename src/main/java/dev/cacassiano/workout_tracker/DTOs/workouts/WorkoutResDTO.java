package dev.cacassiano.workout_tracker.DTOs.workouts;

import java.time.LocalDateTime;
import java.util.Set;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseResDTO;
import dev.cacassiano.workout_tracker.entities.Workout;

public record WorkoutResDTO(
    Long id,
    String title,
    LocalDateTime scheduled_date,
    String schedule_type,
    Boolean completed,
    Set<ExerciseResDTO> exercises
) {
    public WorkoutResDTO(Workout w){
        this(
            w.getId(),
            w.getTitle(),
            w.getScheduledDate(),
            w.getScheduleType(),
            w.getCompleted(),
            ExerciseResDTO.convertAll(w.getExercises())
        );
    }
}
