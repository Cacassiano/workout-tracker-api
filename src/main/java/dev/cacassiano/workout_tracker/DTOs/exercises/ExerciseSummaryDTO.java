package dev.cacassiano.workout_tracker.DTOs.exercises;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import dev.cacassiano.workout_tracker.entities.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExerciseSummaryDTO{

    private Long id;
    private String title;
    private String category;
    private Integer respetitions;
    private Integer series;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public ExerciseSummaryDTO(Exercise e){
        this.id = e.getId();
        this.title = e.getTitle();
        this.category = e.getCategory();
        this.respetitions = e.getReps();
        this.series = e.getSeries();
        this.created_at = e.getCreatedAt();
        this.updated_at = e.getUpdatedAt();
    }

    public static Set<ExerciseSummaryDTO> convertAll(Collection<Exercise> exercises){
        return exercises.stream()
            .map(ExerciseSummaryDTO::new)
            .collect(Collectors.toSet());
    }
}
