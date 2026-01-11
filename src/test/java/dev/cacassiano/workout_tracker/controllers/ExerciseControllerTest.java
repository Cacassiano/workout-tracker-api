package dev.cacassiano.workout_tracker.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import dev.cacassiano.workout_tracker.services.ExerciseService;
import dev.cacassiano.workout_tracker.services.WorkoutService;
import dev.cacassiano.workout_tracker.services.auth.JwtService;
import dev.cacassiano.workout_tracker.services.auth.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.http.MediaType;
import java.util.Collections;

@SpringBootTest
@DisplayName("ExerciseController Tests")
@AutoConfigureMockMvc
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExerciseService exerciseService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WorkoutService workoutService;

    private User mockUser;
    private Exercise mockExercise;
    private Workout mockWorkout;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setEmail("test@email.com");
        mockUser.setUsername("testuser");
        mockUser.setPassword("hashed_password");
        mockUser.setRole("ROLE_USER");
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());

        mockExercise = new Exercise();
        mockExercise.setId(1L);
        mockExercise.setTitle("Pushup");
        mockExercise.setCategory("Chest");
        mockExercise.setReps(12);
        mockExercise.setSeries(4);
        mockExercise.setCreatedAt(LocalDateTime.now());
        mockExercise.setUpdatedAt(LocalDateTime.now());
        mockExercise.setWorkouts(new HashSet<>());

        mockWorkout = new Workout();
        mockWorkout.setId(1L);
        mockWorkout.setTitle("Test Workout");
        mockWorkout.setScheduledDate(LocalDate.now());
        mockWorkout.setScheduleType("WEEKLY");
        mockWorkout.setCompleted(false);
        mockWorkout.setCreatedAt(LocalDateTime.now());
        mockWorkout.setUpdatedAt(LocalDateTime.now());
        mockWorkout.setUser(mockUser);
        mockWorkout.setExercises(new HashSet<>());

        when(jwtService.getIdFromToken(anyString())).thenReturn(mockUser.getId());
        when(userService.getUserReferenceById(mockUser.getId())).thenReturn(mockUser);
    }

    // ============ GET ALL EXERCISES TESTS ============

    @Test
    @DisplayName("GET /api/exercises - Success with workouts and custom user")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesWithWorkoutsAndCustomUser() throws Exception {
        mockExercise.setWorkouts(Set.of(mockWorkout));
        
        Page<Exercise> exercisePage = new PageImpl<>(List.of(mockExercise), PageRequest.of(0, 10), 1);
        when(exerciseService.getAll(any(PageRequest.class), eq(mockUser), eq(true)))
            .thenReturn(exercisePage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "true")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].title").value("Pushup"))
            .andExpect(jsonPath("$.data[0].category").value("Chest"));
    }

    @Test
    @DisplayName("GET /api/exercises - Success without workouts and custom user")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesWithoutWorkoutsAndCustomUser() throws Exception {
        Page<Exercise> exercisePage = new PageImpl<>(List.of(mockExercise), PageRequest.of(0, 10), 1);
        when(exerciseService.getAll(any(PageRequest.class), eq(mockUser), eq(false)))
            .thenReturn(exercisePage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "false")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].title").value("Pushup"))
            .andExpect(jsonPath("$.data[0].category").value("Chest"));
    }

    @Test
    @DisplayName("GET /api/exercises - Success with workouts and default exercises only")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesWithWorkoutsAndDefaultsOnly() throws Exception {
        mockExercise.setWorkouts(Set.of(mockWorkout));
        
        Page<Exercise> exercisePage = new PageImpl<>(List.of(mockExercise), PageRequest.of(0, 10), 1);
        when(exerciseService.getAll(any(PageRequest.class), isNull(), eq(true)))
            .thenReturn(exercisePage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "true")
                .param("onlyDefaults", "true")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].workouts").isArray())
            .andExpect(jsonPath("$.data[0].workouts[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/exercises - Success without workouts and default exercises only")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesWithoutWorkoutsAndDefaultsOnly() throws Exception {
        Page<Exercise> exercisePage = new PageImpl<>(List.of(mockExercise), PageRequest.of(0, 10), 1);
        when(exerciseService.getAll(any(PageRequest.class), isNull(), eq(false)))
            .thenReturn(exercisePage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "false")
                .param("onlyDefaults", "true")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].reps").value(12));
    }

    @Test
    @DisplayName("GET /api/exercises - Success with pagination")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesWithPagination() throws Exception {
        Exercise exercise2 = new Exercise();
        exercise2.setId(2L);
        exercise2.setTitle("Squats");
        exercise2.setCategory("Legs");
        exercise2.setReps(15);
        exercise2.setSeries(4);
        exercise2.setCreatedAt(LocalDateTime.now());
        exercise2.setUpdatedAt(LocalDateTime.now());
        exercise2.setWorkouts(new HashSet<>());

        Page<Exercise> exercisePage = new PageImpl<>(
            List.of(mockExercise, exercise2),
            PageRequest.of(0, 2),
            2
        );
        when(exerciseService.getAll(any(PageRequest.class), eq(mockUser), eq(false)))
            .thenReturn(exercisePage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "false")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "2")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.page_number").value(0))
            .andExpect(jsonPath("$.page_size").value(2));
    }

    @Test
    @DisplayName("GET /api/exercises - Success with empty result")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesEmptyResult() throws Exception {
        Page<Exercise> exercisePage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(exerciseService.getAll(any(PageRequest.class), eq(mockUser), eq(false)))
            .thenReturn(exercisePage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "false")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/exercises - Returns empty list when no exercises found")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetAllExercisesError() throws Exception {
        Page<Exercise> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(exerciseService.getAll(any(PageRequest.class), any(User.class), anyBoolean()))
            .thenReturn(emptyPage);

        mockMvc.perform(
            get("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .param("withWorkouts", "false")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(0));
    }

    // ============ POST TESTS ============

    @Test
    @DisplayName("POST /api/exercises - Success")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testCreateExerciseSuccess() throws Exception {
        ExerciseReqDTO reqDTO = new ExerciseReqDTO(
            "Push-up",
            12,
            4,
            "Chest",
            Collections.emptySet()
        );

        when(workoutService.filterAndGetWorkoutRefs(any(), any())).thenReturn(Collections.emptySet());
        when(exerciseService.saveExercise(any(User.class), any(ExerciseReqDTO.class), any()))
            .thenReturn(mockExercise);

        mockMvc.perform(
            post("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("Pushup"));
    }

    @Test
    @DisplayName("POST /api/exercises - Error with invalid request body")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testCreateExerciseInvalidRequest() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(
            post("/api/exercises")
                .header("Authorization", "Bearer mockToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody)
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    // ============ PUT TESTS ============

    @Test
    @DisplayName("PUT /api/exercises/{id} - Success")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testUpdateExerciseSuccess() throws Exception {
        ExerciseReqDTO reqDTO = new ExerciseReqDTO(
            "Updated Push-up",
            15,
            5,
            "Upper Body",
            Collections.emptySet()
        );

        Exercise updatedExercise = new Exercise();
        updatedExercise.setId(1L);
        updatedExercise.setTitle("Updated Push-up");
        updatedExercise.setReps(15);
        updatedExercise.setSeries(5);
        updatedExercise.setCategory("Upper Body");
        updatedExercise.setCreatedAt(LocalDateTime.now());
        updatedExercise.setUpdatedAt(LocalDateTime.now());
        updatedExercise.setWorkouts(new HashSet<>());

        when(workoutService.filterAndGetWorkoutRefs(any(), any())).thenReturn(Collections.emptySet());
        when(exerciseService.updateExercise(eq(1L), any(ExerciseReqDTO.class), any(User.class), any()))
            .thenReturn(updatedExercise);

        mockMvc.perform(
            put("/api/exercises/1")
                .header("Authorization", "Bearer mockToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("Updated Push-up"))
            .andExpect(jsonPath("$.data.reps").value(15));
    }

    @Test
    @DisplayName("PUT /api/exercises/{id} - Error with invalid request body")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testUpdateExerciseInvalidRequest() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(
            put("/api/exercises/1")
                .header("Authorization", "Bearer mockToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody)
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    // ============ DELETE TESTS ============

    @Test
    @DisplayName("DELETE /api/exercises/{id} - Success")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testDeleteExerciseSuccess() throws Exception {
        doNothing().when(exerciseService).deleteExerciseById(eq(1L), eq(mockUser.getId()));

        mockMvc.perform(
            delete("/api/exercises/1")
                .header("Authorization", "Bearer mockToken")
        )
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/exercises/{id} - Error with exercise not found")
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testDeleteExerciseNotFound() throws Exception {
        doThrow(new NotFoundException("Exercise not found for id: 999"))
            .when(exerciseService).deleteExerciseById(eq(999L), eq(mockUser.getId()));

        mockMvc.perform(
            delete("/api/exercises/999")
                .header("Authorization", "Bearer mockToken")
        )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Exercise not found for id: 999"))
            .andDo(print());
    }
}
