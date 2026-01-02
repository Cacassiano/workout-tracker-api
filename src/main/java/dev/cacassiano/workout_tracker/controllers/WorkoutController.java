package dev.cacassiano.workout_tracker.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.DataDTO;
import dev.cacassiano.workout_tracker.DTOs.pagination.PageDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.PatchStatusDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutResDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutSummaryDTO;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import dev.cacassiano.workout_tracker.services.WorkoutService;
import dev.cacassiano.workout_tracker.services.auth.JwtService;
import dev.cacassiano.workout_tracker.services.auth.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/workouts")
@Slf4j
@SecurityScheme(bearerFormat = "JWT", description = "Login token", type = SecuritySchemeType.HTTP, name="AuthToken", scheme = "bearer")
@SecurityRequirement(name = "AuthToken")
public class WorkoutController {
    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Get all workouts Sucessefuly")
    public ResponseEntity<PageDTO<? extends WorkoutSummaryDTO>> getAllWorkouts(
        @RequestParam("showExercises") Boolean withExercises, 
        @RequestParam("onlyDefaults") Boolean onlyDefaults,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") String token,
        Pageable pageable
    ) {
        
        // Get userId from token
        String userId = jwtService.getIdFromToken(token.substring(7));
        log.info("Get userId {}", userId);
        User user = onlyDefaults ? null : userService.getUserReferenceById(userId);
        // Validate withExercises Query param
        withExercises = withExercises == null ? false : withExercises;

        // find the workouts and map then to DTO version
        log.info("Starting the getAllWorkouts funtion");

        Page<Workout> workoutPage = workoutService.getAllWorkouts(user, withExercises, pageable);

        if (!withExercises) {
            Collection<WorkoutSummaryDTO> data = workoutPage.getContent().stream().map(WorkoutSummaryDTO::new).toList();
            PageDTO<WorkoutSummaryDTO> dto = new PageDTO<>(workoutPage, data);

            return ResponseEntity.ok(dto);
        } 
        
        Collection<WorkoutResDTO> data = workoutPage.getContent().stream().map(WorkoutResDTO::new).toList();
        PageDTO<WorkoutResDTO> dto = new PageDTO<>(workoutPage, data);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Create the workout sucessesfuly")
    public ResponseEntity<DataDTO<WorkoutResDTO>> createWorkout(
        @RequestBody @Valid WorkoutReqDTO req, 
        @Parameter(hidden = true)
        @RequestHeader("Authorization") String token
    ) throws MethodArgumentNotValidException {

        // Get userId from token and Get a hibernate proxy from id
        String userId = jwtService.getIdFromToken(token.substring(7));
        log.info("Get user id {}", userId);
        User user = userService.getUserReferenceById(userId);

        log.info("Starting the createWorkout funtion");
        // Try to save workout on db
        Workout workout = workoutService.saveWorkout(req, user);

        // Construct DTO
        log.info("Creating DTO with workout: {}", workout);
        WorkoutResDTO dto = new WorkoutResDTO(workout);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new DataDTO<>(dto));
    }
    

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Update the entity workout sucessesfuly")
    public ResponseEntity<DataDTO<WorkoutResDTO>> updateWorkout(
        @PathVariable Long id, 
        @RequestBody @Valid WorkoutReqDTO req,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") String token
    ) throws MethodArgumentNotValidException{
        // Get userId from the token
        String userId = jwtService.getIdFromToken(token.substring(7));
        log.info("Get user id {}", userId);

        User user = userService.getUserReferenceById(userId);

        log.info("Starting the updateWorkout funtion");
        // Pass the workout and user's id for update workout

        Workout workout = workoutService.updateWorkout(req, id, user);

        // Creating DTO
        log.info("Creating DTO with workout: {}", workout);
        WorkoutResDTO res = new WorkoutResDTO(workout);

        return ResponseEntity.ok(new DataDTO<>(res));
    }

    @PatchMapping("/{id}/status")
    @ApiResponse(responseCode = "200", description = "Update sucessfuly the status of the workout")
    public ResponseEntity<Void> updateWorkoutStatus(
        @RequestBody @Valid PatchStatusDTO req,
        @PathVariable Long id,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") String token
    ) throws NotFoundException, MethodArgumentNotValidException{
        // Get userId from token
        String userId = jwtService.getIdFromToken(token.substring(7));
        log.info("Get user id {}", userId);
        log.info("Starting the updateWorkoutStatus funtion");
        // Updating the workout's status
        workoutService.updateStatus(req.getCompleted(), id, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "delete sucessfuly the status of the workout")
    public ResponseEntity<Void> deleteWorkout(
        @PathVariable Long id, 
        @Parameter(hidden = true)
        @RequestHeader("Authorization") String token
    )  throws NotFoundException{
        // Get userId from token
        String userId = jwtService.getIdFromToken(token.substring(7));
        log.info("Get user id {}", userId);
        // Try to delete workout
        log.info("Starting the deleteWorkout funtion");
        workoutService.deleteWorkoutById(id, userId);

        return ResponseEntity.noContent().build();
    }
}
