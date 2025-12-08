package dev.cacassiano.workout_tracker.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "workouts")
@Entity(name = "workout")
@Getter
@NoArgsConstructor
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "exercises",
        joinColumns = @JoinColumn(name="workout_id"),
        inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private Set<Exercise> exercises;

    @Column(name = "title" , unique = false, nullable = false)
    private String title;

    @Column(name = "scheduled_date", unique = false, nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "created_at", unique = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", unique = false, nullable = false)
    private LocalDateTime updatedAt;


}
