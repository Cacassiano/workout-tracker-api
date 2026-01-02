package dev.cacassiano.workout_tracker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseResDTO;
import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseSummaryDTO;
import dev.cacassiano.workout_tracker.DTOs.pagination.PageDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.services.ExerciseService;
import dev.cacassiano.workout_tracker.services.auth.JwtService;
import dev.cacassiano.workout_tracker.services.auth.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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

    @GetMapping
    @ApiResponse(responseCode = "200", description = "gets the workouts")
    public ResponseEntity<PageDTO<? extends ExerciseSummaryDTO>> getAll(
        Pageable pageable,
        @RequestParam("withWorkouts") Boolean withWorkouts,
        @RequestParam("onlyDefaults") Boolean onlyDefaults,
        @Parameter(hidden = true) @RequestHeader("Authorization") String token
    ) {
        log.info("starting getAll exercises with the params:\nPageable="+pageable.toString()+"\nwithWorkouts="+withWorkouts+"'\nonlyDefaults="+onlyDefaults);
        String userId = jwtService.getIdFromToken(token.substring(7));
        log.info("userId: {}", userId);
        User user = onlyDefaults ? null : userService.getUserReferenceById(userId);

        Page<Exercise> exercisePage = exerciseService.getAll(pageable, user, withWorkouts);
        log.info("find a page with {} elements", exercisePage.getNumberOfElements());
        if (onlyDefaults) {
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
}