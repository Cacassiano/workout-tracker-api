package dev.cacassiano.workout_tracker.DTOs.exercises;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

@Getter
public class ExerciseReferenceReqDTO {

    private Long id;
    private String title;
    private String category;
    private Integer reps;
    private Integer series;


    public ExerciseReferenceReqDTO(Long id, String title, String category,Integer reps, Integer series) throws ConstraintViolationException{
        if (id != null && id>0) {
            this.id = id;
            return;
        }
        if (id == null && (title != null && !title.isBlank() && reps != null && series != null && category != null && !category.isBlank())){
            this.title = title;
            this.reps = reps;
            this.series = series;
            this.category = category;
            return;
        }
        
        throw new ConstraintViolationException("Invalid request", null);
    }
}
