package dev.cacassiano.workout_tracker.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReferenceReqDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import dev.cacassiano.workout_tracker.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private ExerciseService exerciseService;

    private Set<Exercise> getExerciseReferences(Set<ExerciseReferenceReqDTO> reqExercises, User user) {
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
    public void updateStatus(Boolean completed, Long id, String userId) throws NotFoundException {
        if(workoutRepository.updateStatus(completed, id, userId) == 0 ){
            throw new NotFoundException("Workout not found for id: "+ id);
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
    public void deleteWorkoutById(Long id, String user_id) throws NotFoundException{
        if(workoutRepository.deleteByIdAndCount(id) == 0){
            throw new NotFoundException("Workout not found for id: "+ id);
        }
    }

    // SELECT
    @Transactional
    public Page<Workout> getAllWorkouts(User user, Boolean withExercises, Pageable pageable) {
        if (withExercises) {
            return workoutRepository.findAllWithExercises(user, pageable);
        }
        return workoutRepository.findAllWorkouts(user, pageable);
    }
}
