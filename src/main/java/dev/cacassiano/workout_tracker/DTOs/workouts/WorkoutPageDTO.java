package dev.cacassiano.workout_tracker.DTOs.workouts;

import java.util.Collection;

import org.springframework.data.domain.Page;

import dev.cacassiano.workout_tracker.DTOs.pagination.PageDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutPageDTO<DTO extends WorkoutSummaryDTO> extends PageDTO{

    Collection<DTO> data;

    public WorkoutPageDTO(Page<?> page, Collection<DTO> data) {
        super(page);
        this.data = data;
    }
    
}
