package dev.cacassiano.workout_tracker.DTOs.workouts;

import java.time.LocalDateTime;
import dev.cacassiano.workout_tracker.entities.Workout;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutSummaryDTO{
    Long id;
    String title;
    LocalDateTime scheduled_date;
    String schedule_type;
    Boolean completed;
    LocalDateTime created_at;
    LocalDateTime updated_at;

    public WorkoutSummaryDTO(Workout w){
        this.id = w.getId();
        this.title = w.getTitle();
        this.scheduled_date = w.getScheduledDate();
        this.schedule_type = w.getScheduleType();
        this.completed = w.getCompleted();
        this.created_at = w.getCreatedAt();
        this.updated_at = w.getUpdatedAt();
    }
}
