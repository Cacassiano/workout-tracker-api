package dev.cacassiano.workout_tracker.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;


@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    // Especifica como uma query que modifica o banco (C, U, D)
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM workout_exercises WHERE workout_id=:id;\nDELETE FROM workouts WHERE id=:id;")
    Long deleteByIdAndCount(@Param("id") Long id);

    @Query(value = "SELECT w FROM workout w LEFT JOIN FETCH w.exercises WHERE w.user IS NOT DISTINCT FROM :user")
    Page<Workout> findAllWithExercises(@Param("user") User user, Pageable pageable);

    @Query(value = "SELECT w FROM workout w WHERE w.user IS NOT DISTINCT FROM :user")
    Page<Workout> findAllWorkouts(@Param("user") User user, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE workouts SET completed=:completed, updated_at = NOW() WHERE id=:id and user_id = :userId", nativeQuery = true)
    int updateStatus(@Param("completed") Boolean completed, @Param("id")Long id, @Param("userId") String userId);

    @Query(value = "SELECT w FROM workout w WHERE w.user.id=:userId AND w.id=:id LIMIT 1")
    @EntityGraph(attributePaths = {})
    Workout getReferenceByIdAndUser(@Param("id") Long id, @Param("userId") String userId);
}
