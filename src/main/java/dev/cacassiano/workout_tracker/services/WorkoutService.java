package dev.cacassiano.workout_tracker.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReqDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private ExerciseService exerciseService;

    private Set<Exercise> getExerciseReferences(Set<ExerciseReqDTO> reqExercises, User user) {
        Set<Exercise> exercises = new HashSet<>();
        reqExercises.forEach(e -> {
            if (e.getId() != null) {
                Exercise ex = exerciseService.findExerciseById(e.getId(), user.getId());
                if (ex == null) {
                    return;
                }
                exercises.add(ex);
                return;
            }
            exercises.add(new Exercise(e, user));
        });

        return exercises;
    }

    // UPDATE and INSERT
    // TODO Exception Handler
    public void updateStatus(Boolean completed, Long id, String userId) {
        if(workoutRepository.updateStatus(completed, id) == 0 ){
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public Workout updateWorkout(WorkoutReqDTO req, Long id, User user){
        Set<Exercise> exercises = this.getExerciseReferences(req.getExercises(), user);

        Workout workout = workoutRepository.getReferenceByIdAndUser(id, user.getId());
        workout.update(req, exercises, null);


        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout saveWorkout(WorkoutReqDTO req, User user) {
        Set<Exercise> exercises = this.getExerciseReferences(req.getExercises(), user);
        
        Workout workout = new Workout();
        workout.update(req, exercises, user);

        return workoutRepository.save(workout);
    }

    // DELETE
    @Transactional
    public void deleteWorkoutById(Long id, String user_id){
        // TODO ERROR Handling
        if(workoutRepository.deleteByIdAndCount(id) == 0){
            throw new RuntimeException("User not found");
        }
    }

    // SELECT
    @Transactional
    public List<Workout> getAllWorkouts(String userId, Boolean withExercises) {
        return workoutRepository.findAllWithExercises(userId);
    }
}
