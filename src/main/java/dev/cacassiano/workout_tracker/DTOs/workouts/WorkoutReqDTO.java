package dev.cacassiano.workout_tracker.DTOs.workouts;

import java.time.LocalDate;
import java.util.Set;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReferenceReqDTO;
import dev.cacassiano.workout_tracker.services.enums.ScheduleTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WorkoutReqDTO {
    
    @NotBlank(message = "title cannot be blank")
    private final String title;

    @NotBlank(message = "schedule_type cannot be blank")
    private final String schedule_type;

    @NotNull(message = "schedule_date cannot be null")
    private final LocalDate scheduled_date;

    @NotNull(message = "completed cannot be null")
    private final Boolean completed;

    @NotEmpty(message = "exercises cannot be empty")
    private final Set<ExerciseReferenceReqDTO> exercises;

    public WorkoutReqDTO(String title, String schedule_type, LocalDate scheduled_date, Boolean completed, Set<ExerciseReferenceReqDTO> exercises){
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
