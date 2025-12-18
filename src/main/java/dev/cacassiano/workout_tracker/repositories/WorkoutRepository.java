package dev.cacassiano.workout_tracker.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Workout;


@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    // Especifica como uma query que modifica o banco (C, U, D)
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM workout_exercises WHERE workout_id=:id;\nDELETE FROM workouts WHERE id=:id;")
    void deleteById(@Param("id") Long id);

    @Query(value = "SELECT w FROM workout w JOIN FETCH w.exercises")
    List<Workout> findAllWithExercises();

    @Modifying
    @Query(value = "UPDATE workouts SET completed=:completed WHERE id=:id", nativeQuery = true)
    void updateStatus(@Param("completed") Boolean completed, @Param("id")Long id);
}
