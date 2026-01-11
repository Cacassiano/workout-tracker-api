package dev.cacassiano.workout_tracker.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.cacassiano.workout_tracker.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    // Query for scheduler
    // TODO validate monthly workouts (add a condiotion for it)
    // TODO Add limit for batch
    @Query(value = "SELECT u FROM user u LEFT JOIN u.workouts w WHERE w.scheduledDayOfWeek=:weekDay OR w.scheduledDate=:date OR w.scheduleType='DAILY'")
    List<User> findAllByPendingNotificationWorkout(@Param("weekDay") Integer weekDay, @Param("date") LocalDate date);
}
