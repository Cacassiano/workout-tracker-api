package dev.cacassiano.workout_tracker.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
    @Query(nativeQuery = true, value = "SELECT * from exercises WHERE id=:id and user_id=:userId LIMIT 1")
    Optional<Exercise> findByIdAndUser(@Param("id") Long id, @Param("userId") String userId);

    // Get all methods
    @Query("SELECT e FROM exercise e LEFT JOIN FETCH e.workouts WHERE e.user IS NOT DISTINCT FROM :user")
    Page<Exercise> findAllAndFetchWorkouts(Pageable pageable, @Param("user") User user);

    @Query("SELECT e FROM exercise e WHERE e.user IS NOT DISTINCT FROM :user")
    Page<Exercise> findAllExercise(Pageable pageable, @Param("user") User user);

    @Modifying
    @Query(value = "DELETE FROM exercise e WHERE e.id=:id AND e.user.id=:userId")
    Long deleteExerciseById(@Param("id") Long id, @Param("userId") String userId);
}
