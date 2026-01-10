package dev.cacassiano.workout_tracker.DTOs.workouts;

import java.time.LocalDateTime;

import dev.cacassiano.workout_tracker.services.enums.ScheduleTypes;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

@Getter
public class WorkoutReferenceDTO {
    private Long id;

    private String title;

    private String schedule_type;

    private LocalDateTime scheduled_date;

    private Boolean completed;

    public WorkoutReferenceDTO(Long id, String title, String schedule_type, LocalDateTime scheduled_date, Boolean completed){
        if (id != null) {
            this.id = id;
            return;
        }
        if (title != null && !title.isBlank() && schedule_type != null && scheduled_date != null && completed != null) {
            this.title = title;
            this.schedule_type  = schedule_type.toUpperCase();
            // Validate if its a valid schedule_type
            ScheduleTypes.valueOf(this.schedule_type);
            this.scheduled_date = scheduled_date;
            this.completed = completed;
            return;
        }
        new ConstraintViolationException("invalid request", null);
    }
}
