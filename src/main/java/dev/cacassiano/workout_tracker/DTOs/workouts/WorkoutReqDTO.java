package dev.cacassiano.workout_tracker.DTOs.workouts;

import java.time.LocalDateTime;
import java.util.Set;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReferenceReqDTO;
import dev.cacassiano.workout_tracker.services.enums.ScheduleTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WorkoutReqDTO {
    
    @NotBlank
    private final String title;

    @NotBlank
    private final String schedule_type;

    @NotNull
    private final LocalDateTime scheduled_date;

    @NotNull
    private final Boolean completed;

    private final Set<ExerciseReferenceReqDTO> exercises;

    public WorkoutReqDTO(String title, String schedule_type, LocalDateTime scheduled_date, Boolean completed, Set<ExerciseReferenceReqDTO> exercises){
        this.title = title;
        if (schedule_type == null) {
            throw new IllegalArgumentException("schedule_type cant be null");
        }
        ScheduleTypes.valueOf(schedule_type.toUpperCase());
        this.schedule_type = schedule_type.toUpperCase();
        this.scheduled_date = scheduled_date;
        this.completed = completed;
        this.exercises = exercises;
    }
    
}
