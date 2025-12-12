package dev.cacassiano.workout_tracker.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    @Query(value = "DELETE FROM workouts WHERE id=:id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

    @Query(value = "SELECT w FROM workout w JOIN FETCH w.exercises")
    List<Workout> findAllWithExercises();
}
