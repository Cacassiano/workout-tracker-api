package dev.cacassiano.workout_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
    
}
