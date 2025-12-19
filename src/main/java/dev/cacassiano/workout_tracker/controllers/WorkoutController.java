package dev.cacassiano.workout_tracker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.PatchStatusDTO;
import dev.cacassiano.workout_tracker.DTOs.ResponseDTO;
import dev.cacassiano.workout_tracker.DTOs.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.DTOs.WorkoutResDTO;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.services.WorkoutService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/workouts")
@Slf4j
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get all workouts Sucessefuly"),
        @ApiResponse(responseCode = "500", description = "Can't get the workouts from db")
    })
    public ResponseEntity<ResponseDTO<List<WorkoutResDTO>>> getAllWorkouts() {
        log.info("Starting the getAllWorkouts funtion");
        List<WorkoutResDTO> workouts = workoutService.getAllWorkouts().stream()
            .map(WorkoutResDTO::new)
            .toList();

        return ResponseEntity.ok(new ResponseDTO<>(workouts));
    }


    @PostMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Create the workout sucessesfuly"),
        @ApiResponse(responseCode = "400", description = "Invalid or missing values on the request body"),
        @ApiResponse(responseCode = "500", description = "Can't insert the workouts into the db")
    })
    public ResponseEntity<ResponseDTO<WorkoutResDTO>> createWorkout(@RequestBody @Valid WorkoutReqDTO req) {
        log.info("Starting the createWorkout funtion");
        Workout workout = workoutService.saveWorkout(req);
        WorkoutResDTO dto = new WorkoutResDTO(workout);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ResponseDTO<>(dto));
    }
    

    @PutMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update the entity workout sucessesfuly"),
        @ApiResponse(responseCode = "400", description = "Invalid or missing values in the request body"),
        @ApiResponse(responseCode = "500", description = "Something went wrong when updating")
    })
    public ResponseEntity<ResponseDTO<WorkoutResDTO>> updateWorkout(@PathVariable Long id, @RequestBody @Valid WorkoutReqDTO req) {
        log.info("Starting the updateWorkout funtion");
        Workout workout = workoutService.updateWorkout(req, id);
        WorkoutResDTO res = new WorkoutResDTO(workout);

        return ResponseEntity.ok(new ResponseDTO<>(res));
    }

    @PatchMapping("/{id}/status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update sucessfuly the status of the workout"),
        @ApiResponse(responseCode = "400", description = "Invalid or missing values in the request body"),
        @ApiResponse(responseCode = "500", description = "Something went wrong when updating")
    })
    public ResponseEntity<Void> updateWorkoutStatus(
        @RequestBody @Valid @NotNull PatchStatusDTO req,
        @PathVariable Long id
    ) {
        log.info("Starting the updateWorkoutStatus funtion");
        // TODO retornar o workout e devolver para o usuário
        workoutService.updateStatus(req.getCompleted(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "delete sucessfuly the status of the workout"),
        @ApiResponse(responseCode = "400", description = "Invalid or missing id")
    })
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id){
        log.info("Starting the deleteWorkout funtion");
        workoutService.deleteWorkoutById(id);
        return ResponseEntity.noContent().build();
    }
}
