package dev.cacassiano.workout_tracker.use_cases.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReferenceReqDTO;
import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import dev.cacassiano.workout_tracker.repositories.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;
    
    // Get all method
    public Page<Exercise> getAll(Pageable pageable, User user, Boolean withWorkouts) {
        if (withWorkouts) {
            return exerciseRepository.findAllAndFetchWorkouts(pageable, user);
        }
        return exerciseRepository.findAllExercise(pageable, user);
    }
    // Create method
    public Exercise saveExercise(User user, ExerciseReqDTO req, Set<Workout> workouts) {
        return exerciseRepository.save(new Exercise(req, user, workouts));
    }

    public Exercise findExerciseById(Long id, String userId) {
        return exerciseRepository.findByIdAndUser(id, userId).get();
    }

    public void deleteExerciseById(Long id, String userId) {
        long deletedCount = exerciseRepository.deleteExerciseById(id, userId);
        if (deletedCount <=0) {
            throw new NotFoundException("exercise not found for id: "+id);
        }
    }

    public Exercise updateExercise(Long id, ExerciseReqDTO req, User user, Set<Workout> workouts) {
        Exercise ex = findExerciseById(id, user.getId());
        if (ex == null) {
            throw new NotFoundException("exercise not found for id:"+id);
        }
        ex.update(req, user, workouts);
        return exerciseRepository.save(ex);
    }

    public Set<Exercise> getExerciseReferences(Set<ExerciseReferenceReqDTO> reqExercises, User user) {
        Set<Exercise> exercises = new HashSet<>();
        if (reqExercises == null || reqExercises.isEmpty()) {
            log.info("returning a empty exercise ref set");
            return exercises;
        }
        
        reqExercises.forEach(e -> {
            if (e.getId() != null) {
                Exercise ex = findExerciseById(e.getId(), user.getId());
                if (ex == null) {
                    return;
                }
                exercises.add(ex);
                return;
            }
            exercises.add(new Exercise(e, user));
        });
        log.info("returning a {} length exercise set", exercises.size());
        return exercises;
    } 
}
