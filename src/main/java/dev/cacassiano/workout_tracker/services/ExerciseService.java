package dev.cacassiano.workout_tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.repositories.ExerciseRepository;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    public Exercise findExerciseById(Long id, String userId) {
        return exerciseRepository.findByIdAndUser(id, userId).get();
    }
}
