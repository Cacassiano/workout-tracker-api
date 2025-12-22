package dev.cacassiano.workout_tracker.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReqDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
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

    private Set<Exercise> getExerciseReferences(Set<ExerciseReqDTO> reqExercises) {
        Set<Exercise> exercises = new HashSet<>();
        reqExercises.forEach(e -> {
            if (e.getId() != null) {
                exercises.add(exerciseService.getExerciseReferenceById(e.getId()));
                return;
            }
            exercises.add(new Exercise(e));
        });

        return exercises;
    }

    // UPDATE and INSERT
    public void updateStatus(Boolean completed, Long id) {
        workoutRepository.updateStatus(completed, id);
    }

    @Transactional
    public Workout updateWorkout(WorkoutReqDTO req, Long id){
        Set<Exercise> exercises = this.getExerciseReferences(req.getExercises());

        Workout workout = workoutRepository.getReferenceById(id);
        workout.update(req, exercises);

        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout saveWorkout(WorkoutReqDTO req) {
        Set<Exercise> exercises = this.getExerciseReferences(req.getExercises());
        
        Workout workout = new Workout();
        workout.update(req, exercises);

        return workoutRepository.save(workout);
    }

    // DELETE
    @Transactional
    public void deleteWorkoutById(Long id){
        workoutRepository.deleteById(id);
    }

    // SELECT
    @Transactional
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAllWithExercises();
    }
}
