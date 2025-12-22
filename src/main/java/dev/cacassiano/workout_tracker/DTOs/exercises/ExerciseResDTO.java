package dev.cacassiano.workout_tracker.DTOs.exercises;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import dev.cacassiano.workout_tracker.entities.Exercise;

public record ExerciseResDTO(
    Long id,
    String title,
    Integer respetitions,
    Integer series
) {
    public ExerciseResDTO(Exercise e){
        this(
            e.getId(),
            e.getTitle(),
            e.getReps(),
            e.getSeries()
        );
    }

    public static Set<ExerciseResDTO> convertAll(Collection<Exercise> exercises){
        return exercises.stream()
            .map(ExerciseResDTO::new)
            .collect(Collectors.toSet());
    }
}
