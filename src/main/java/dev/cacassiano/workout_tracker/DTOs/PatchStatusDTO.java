package dev.cacassiano.workout_tracker.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PatchStatusDTO {
    @NotNull
    private final Boolean completed;
}
