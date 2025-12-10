package dev.cacassiano.workout_tracker.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    @Query(nativeQuery = true, value = "SELECT * FROM workouts")
    List<Workout> findAllWorkouts();
}
