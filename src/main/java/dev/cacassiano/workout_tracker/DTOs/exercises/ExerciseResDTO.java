package dev.cacassiano.workout_tracker.DTOs.exercises;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import dev.cacassiano.workout_tracker.entities.Exercise;

public record ExerciseResDTO(
    Long id,
    String title,
    String category,
    Integer respetitions,
    Integer series,
    LocalDateTime created_at,
    LocalDateTime updated_at
) {
    public ExerciseResDTO(Exercise e){
        this(
            e.getId(),
            e.getTitle(),
            e.getCategory(),
            e.getReps(),
            e.getSeries(),
            e.getCreatedAt(),
            e.getUpdatedAt()
        );
    }

    public static Set<ExerciseResDTO> convertAll(Collection<Exercise> exercises){
        return exercises.stream()
            .map(ExerciseResDTO::new)
            .collect(Collectors.toSet());
    }
}
