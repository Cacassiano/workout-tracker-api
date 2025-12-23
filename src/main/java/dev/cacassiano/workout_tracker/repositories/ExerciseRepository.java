package dev.cacassiano.workout_tracker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
    @Query(nativeQuery = true, value = "SELECT * from exercises WHERE id=:id and user_id=:userId LIMIT 1")
    Optional<Exercise> findByIdAndUser(@Param("id") Long id, @Param("userId") String userId);
}
