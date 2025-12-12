package dev.cacassiano.workout_tracker.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
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

    // UPDATE and INSERT
    public Workout saveWorkout(WorkoutReqDTO req) {
        Set<Exercise> exercises = new HashSet<>();
        
        req.getExercises().forEach(e -> {
            if (e.getId() != null) {
                exercises.add(exerciseService.getExerciseReferenceById(e.getId()));
                return;
            }
            exercises.add(new Exercise(e));
        });

        return workoutRepository.save(new Workout(req, exercises));

    }
    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    // DELETE
    public void deleteWorkoutById(Long id){
        workoutRepository.deleteById(id);
    }

    // SELECT
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAllWithExercises();
    }
}
