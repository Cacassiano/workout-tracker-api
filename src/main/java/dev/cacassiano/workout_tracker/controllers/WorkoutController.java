package dev.cacassiano.workout_tracker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.ResponseDTO;
import dev.cacassiano.workout_tracker.DTOs.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.DTOs.WorkoutResDTO;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.services.WorkoutService;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<WorkoutResDTO>>> getAllWorkouts() {
        List<WorkoutResDTO> workouts = workoutService.getAllWorkouts().stream()
            .map(WorkoutResDTO::new)
            .toList();

        return ResponseEntity.ok(new ResponseDTO<>(workouts));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<WorkoutResDTO>> createWorkout(@RequestBody WorkoutReqDTO req) {
        Workout workout = workoutService.saveWorkout(req);
        WorkoutResDTO dto = new WorkoutResDTO(workout);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ResponseDTO<>(dto));
    }
    
}
