package dev.cacassiano.workout_tracker.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReferenceReqDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.PatchStatusDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import dev.cacassiano.workout_tracker.use_cases.services.ExerciseService;
import dev.cacassiano.workout_tracker.use_cases.services.WorkoutService;
import dev.cacassiano.workout_tracker.use_cases.services.auth.JwtService;
import dev.cacassiano.workout_tracker.use_cases.services.auth.UserService;

@SpringBootTest
@DisplayName("WorkoutController Tests")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutService workoutService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private ExerciseService exerciseService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private User mockUser;
    private Workout mockWorkout;
    private String mockToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTE2MjM5MDIyfQ.signature";
    private String mockUserId;
    private UserDetails mockUserDetails;

    @BeforeAll
    void mockSetUp() {
        MockitoAnnotations.openMocks(this);
    } 

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setEmail("testemail@email.com");
        mockUser.setUsername("testuser");
        mockUser.setPassword("hashed_password");
        mockUser.setRole("ROLE_USER");
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());

        mockUserId = mockUser.getId();

        mockUserDetails = new org.springframework.security.core.userdetails.User(
            mockUser.getEmail(),
            mockUser.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority( "ROLE_USER"))
        );

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

        // Security mocks
        when(jwtService.getEmailFromToken(mockToken.substring(7))).thenReturn(mockUser.getEmail());
        when(jwtService.getIdFromToken(mockToken.substring(7))).thenReturn(mockUserId);

        when(userService.getUserReferenceById(mockUserId)).thenReturn(mockUser);
        when(userService.getUserReferenceById(null)).thenReturn(null);

        when(userDetailsService.loadUserByUsername(mockUser.getEmail())).thenReturn(mockUserDetails);
    }

    // ============ GET ALL WORKOUTS TESTS ============

    @Test
    @DisplayName("GET /api/workouts - Success with exercises and custom user")
    
    void testGetAllWorkoutsWithExercisesAndCustomUser() throws Exception {

        Exercise exercise = new Exercise();
        exercise.setId(1L);
        exercise.setTitle("Pushup");
        mockWorkout.setExercises(Set.of(exercise));

        Page<Workout> workoutPage = new PageImpl<>(List.of(mockWorkout), PageRequest.of(0, 10), 1);
        when(workoutService.getAllWorkouts(mockUser, true, PageRequest.of(0, 10))).thenReturn(workoutPage);

        mockMvc.perform(
            get("/api/workouts")
                .header("Authorization", mockToken)
                .param("showExercises", "true")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].title").value("Test Workout"))
            .andExpect(jsonPath("$.data[0].exercises").isArray());
    }

    @Test
    @DisplayName("GET /api/workouts - Success without exercises and custom user")
    void testGetAllWorkoutsWithoutExercisesAndCustomUser() throws Exception {
        
        Page<Workout> workoutPage = new PageImpl<>(List.of(mockWorkout), PageRequest.of(0, 10), 1);
        when(workoutService.getAllWorkouts(mockUser, false, PageRequest.of(0, 10))).thenReturn(workoutPage);

        mockMvc.perform(
            get("/api/workouts")
                .header("Authorization", mockToken)
                .param("showExercises", "false")
                .param("onlyDefaults", "false")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].title").value("Test Workout"));
    }

    @Test
    @DisplayName("GET /api/workouts - Success with exercises and default workouts only")
    void testGetAllWorkoutsWithExercisesAndDefaultsOnly() throws Exception {

        Exercise exercise = new Exercise();
        exercise.setId(1L);
        exercise.setTitle("Pushup");
        mockWorkout.setExercises(Set.of(exercise));

        Page<Workout> workoutPage = new PageImpl<>(List.of(mockWorkout), PageRequest.of(0, 10), 1);
        when(workoutService.getAllWorkouts(null, true, PageRequest.of(0, 10))).thenReturn(workoutPage);

        mockMvc.perform(
            get("/api/workouts")
                .header("Authorization", mockToken)
                .param("showExercises", "true")
                .param("onlyDefaults", "true")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].exercises").isArray());
    }

    @Test
    @DisplayName("GET /api/workouts - Success without exercises and default workouts only")
    void testGetAllWorkoutsWithoutExercisesAndDefaultsOnly() throws Exception {        

        Page<Workout> workoutPage = new PageImpl<>(List.of(mockWorkout), PageRequest.of(0, 10), 1);
        when(workoutService.getAllWorkouts(null, false, PageRequest.of(0, 10))).thenReturn(workoutPage);

        mockMvc.perform(
            get("/api/workouts")
                .header("Authorization", mockToken)
                .param("showExercises", "false")
                .param("onlyDefaults", "true")
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1));
    }

    // ============ POST TESTS ============

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("POST /api/workouts - Success")
    void testCreateWorkoutSuccess() throws Exception {
        WorkoutReqDTO reqDTO = new WorkoutReqDTO(
            "New Workout",
            "WEEKLY",
            LocalDate.now(),
            false,
            Collections.singleton(new ExerciseReferenceReqDTO(1l, null, null, null, null))
        );

        when(workoutService.saveWorkout(any(WorkoutReqDTO.class), any(User.class), any(Set.class))).thenReturn(mockWorkout);
        when(exerciseService.getExerciseReferences(anySet(), any())).thenReturn(Collections.singleton(new Exercise()));
        mockMvc.perform(
            post("/api/workouts")
                .header("Authorization", mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("Test Workout"));
    }

    @Test
    @DisplayName("POST /api/workouts - Error with invalid request body")
    void testCreateWorkoutInvalidRequest() throws Exception {
        String invalidBody = "{}";

        
        mockMvc.perform(
            post("/api/workouts")
                .header("Authorization", mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody)
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    // ============ PUT TESTS ============

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("PUT /api/workouts/{id} - Success")
    void testUpdateWorkoutSuccess() throws Exception {
        WorkoutReqDTO reqDTO = new WorkoutReqDTO(
            "Updated Workout",
            "MONTHLY",
            LocalDate.now(),
            true,
            Collections.singleton(new ExerciseReferenceReqDTO(1l, null, null, null, null))
        );
        
        Workout resWorkout = new Workout(mockWorkout);
        resWorkout.setCompleted(true);
        resWorkout.setTitle("Updated Workout");
        resWorkout.setScheduleType("MONTHLY");

        when(workoutService.updateWorkout(any(WorkoutReqDTO.class), eq(1L), any(User.class), any(Set.class)))
            .thenReturn(resWorkout);
        when(exerciseService.getExerciseReferences(anySet(), any())).thenReturn(Collections.singleton(new Exercise()));

        mockMvc.perform(
            put("/api/workouts/1")
                .header("Authorization", mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("Updated Workout"))
            .andExpect(jsonPath("$.data.schedule_type").value("MONTHLY"))
            .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    @DisplayName("PUT /api/workouts/{id} - Error with invalid request body")
    void testUpdateWorkoutInvalidRequest() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(
            put("/api/workouts/1")
                .header("Authorization", mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody)
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    // ============ PATCH TESTS ============

    @Test
    @DisplayName("PATCH /api/workouts/{id}/status - Success")
    void testPatchWorkoutStatusSuccess() throws Exception {
        PatchStatusDTO patchDTO = new PatchStatusDTO(true);

        doNothing().when(workoutService).updateStatus(eq(true), eq(1L), eq(mockUserId));

        mockMvc.perform(
            patch("/api/workouts/1/status")
                .header("Authorization", mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDTO))
        )
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/workouts/{id}/status - Error with workout not found")
    void testPatchWorkoutStatusNotFound() throws Exception {
        PatchStatusDTO patchDTO = new PatchStatusDTO(true);

        doThrow(new NotFoundException("Workout not found for id: 999"))
            .when(workoutService).updateStatus(eq(true), eq(999L), eq(mockUserId));

        mockMvc.perform(
            patch("/api/workouts/999/status")
                .header("Authorization", mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDTO))
        )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    // ============ DELETE TESTS ============

    @Test
    @DisplayName("DELETE /api/workouts/{id} - Success")
    void testDeleteWorkoutSuccess() throws Exception {
        doNothing().when(workoutService).deleteWorkoutById(eq(1L), eq(mockUserId));

        mockMvc.perform(
            delete("/api/workouts/1")
                .header("Authorization", mockToken)
        )
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/workouts/{id} - Error with workout not found")
    void testDeleteWorkoutNotFound() throws Exception {
        doThrow(new NotFoundException("Workout not found for id: 999"))
            .when(workoutService).deleteWorkoutById(eq(999L), eq(mockUserId));

        mockMvc.perform(
            delete("/api/workouts/999")
                .header("Authorization", mockToken)
        )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Workout not found for id: 999"))
            .andDo(print());
    }
}
