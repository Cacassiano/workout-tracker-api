package dev.cacassiano.workout_tracker.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.DataDTO;
import dev.cacassiano.workout_tracker.DTOs.auth.TokenDTO;
import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReqDTO;
import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseResDTO;
import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseSummaryDTO;
import dev.cacassiano.workout_tracker.DTOs.pagination.PageDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.services.ExerciseService;
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
@RequestMapping("/api/exercises")
@Slf4j
@SecurityScheme(bearerFormat = "JWT", description = "Login token", type = SecuritySchemeType.HTTP, name="AuthToken", scheme = "bearer")
@SecurityRequirement(name = "AuthToken")
public class ExerciseController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private WorkoutService workoutService;    

    @GetMapping
    @ApiResponse(responseCode = "200", description = "gets the Exercises")
    // TODO if a exercise dont have a workout it doesnt appear where withWorkouts = true
    public ResponseEntity<PageDTO<? extends ExerciseSummaryDTO>> getAll(
        Pageable pageable,
        @RequestParam("withWorkouts") Boolean withWorkouts,
        @RequestParam("onlyDefaults") Boolean onlyDefaults,
        @Parameter(hidden = true) @RequestHeader("Authorization") TokenDTO token
    ) {
        log.info("starting getAll exercises with the params:\nPageable="+pageable.toString()+"\nwithWorkouts="+withWorkouts+"'\nonlyDefaults="+onlyDefaults);
        String userId = jwtService.getIdFromToken(token.getToken());
        log.info("userId: {}", userId);
        User user = onlyDefaults ? null : userService.getUserReferenceById(userId);

        Page<Exercise> exercisePage = exerciseService.getAll(pageable, user, withWorkouts);
        log.info("find a page with {} elements", exercisePage.getNumberOfElements());
        if (withWorkouts) {
            List<ExerciseResDTO> exercisesDTO = exercisePage.getContent().stream().map(ExerciseResDTO::new).toList();
            log.info("returning complete exercises DTO");
            PageDTO<ExerciseResDTO> dto = new PageDTO<ExerciseResDTO>(exercisePage, exercisesDTO);
            return ResponseEntity.ok(dto);
        }

        List<ExerciseSummaryDTO> exercisesDTO = exercisePage.getContent().stream().map(ExerciseSummaryDTO::new).toList();
        log.info("returning exercises summary DTO");
        PageDTO<ExerciseSummaryDTO> dto = new PageDTO<ExerciseSummaryDTO>(exercisePage, exercisesDTO);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DataDTO<ExerciseResDTO>> createExercise(
        @RequestBody @Valid ExerciseReqDTO req,
        @Parameter(hidden = true) @RequestHeader("Authorization") TokenDTO token
    ) throws MethodArgumentNotValidException {
        log.info("Starting createExercise method {}", req);
        String userId = jwtService.getIdFromToken(token.getToken());
        log.info("User id: {}", userId);
        User user = userService.getUserReferenceById(userId);
        req.getWorkouts().forEach(w -> log.info(w.toString()));


        // filter the workouts from the request
        Set<Workout> workouts = workoutService.filterAndGetWorkoutRefs(req.getWorkouts(), user);
        Exercise ex = exerciseService.saveExercise(user, req, workouts);
        
        DataDTO<ExerciseResDTO> res = new DataDTO<>(new ExerciseResDTO(ex));

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataDTO<ExerciseResDTO>> updateExercise(
        @PathVariable Long id,
        @Parameter(hidden = true) @RequestHeader("Authorization") TokenDTO token,
        @RequestBody @Valid ExerciseReqDTO req
    ) {
        log.info("starting updateExercise");
        String userId = jwtService.getIdFromToken(token.getToken());
        log.info("encountered the user with the id {}", userId);
        User user = userService.getUserReferenceById(userId);

        Set<Workout> workouts = workoutService.filterAndGetWorkoutRefs(req.getWorkouts(), user);
        Exercise exercise = exerciseService.updateExercise(id, req, user, workouts);
        ExerciseResDTO dto = new ExerciseResDTO(exercise);

        return ResponseEntity.ok(new DataDTO<>(dto));
    }
}