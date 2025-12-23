package dev.cacassiano.workout_tracker.entities;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "workouts")
@Entity(name = "workout")
@Getter
@NoArgsConstructor
public class Workout {

    public void update(WorkoutReqDTO req, Set<Exercise> exercises, User user) {
        this.title = req.getTitle();
        this.scheduleType = req.getSchedule_type();
        this.scheduledDate = req.getScheduled_date();
        this.completed = req.getCompleted();
        this.exercises = exercises;
        if (user != null) {
            this.user = user;
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY )
    @JoinTable(
        name = "workout_exercises",
        joinColumns = @JoinColumn(name="workout_id"),
        inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private Set<Exercise> exercises;

    @Column(name = "title" , unique = false, nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "scheduled_date", unique = false, nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "schedule_type", length = 7, nullable = false, unique = false)
    private String scheduleType;

    @Column(name = "completed", nullable = false, unique = false)
    private Boolean completed;

    @Column(name = "created_at", unique = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", unique = false, nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
