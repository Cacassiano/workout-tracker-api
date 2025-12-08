package dev.cacassiano.workout_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    
}
