package dev.cacassiano.workout_tracker.DTOs;

import lombok.Getter;

@Getter
public class ExerciseReqDTO {

    private Long id;
    private String title;
    private Integer reps;
    private Integer series;


    public ExerciseReqDTO(Long id, String title, Integer reps, Integer series) throws Exception{
        if (id != null && id>0) {
            this.id = id;
            return;
        }
        if (id == null && (title != null && reps != null || series != null)){
            this.title = title;
            this.reps = reps;
            this.series = series;
            return;
        }

        throw new Exception("Invalid request");
    }
}
