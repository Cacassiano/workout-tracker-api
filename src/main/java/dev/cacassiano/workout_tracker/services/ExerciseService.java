package dev.cacassiano.workout_tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.repositories.ExerciseRepository;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    public Page<Exercise> getAll(Pageable pageable, User user, Boolean withWorkouts) {
        if (withWorkouts) {
            return exerciseRepository.findAllAndFetchWorkouts(pageable, user);
        }
        return exerciseRepository.findAllExercise(pageable, user);
    }

    public Exercise findExerciseById(Long id, String userId) {
        return exerciseRepository.findByIdAndUser(id, userId).get();
    }
}
