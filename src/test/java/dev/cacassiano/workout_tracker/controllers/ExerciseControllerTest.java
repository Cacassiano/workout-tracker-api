package dev.cacassiano.workout_tracker.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.services.ExerciseService;
import dev.cacassiano.workout_tracker.services.auth.JwtService;
import dev.cacassiano.workout_tracker.services.auth.UserService;

@SpringBootTest
@DisplayName("ExerciseController Tests")
@AutoConfigureMockMvc
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExerciseService exerciseService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

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
        mockWorkout.setScheduledDate(LocalDateTime.now());
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
            .andExpect(jsonPath("$.data[0].respetitions").value(12));
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
}
