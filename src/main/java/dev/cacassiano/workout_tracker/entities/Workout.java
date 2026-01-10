package dev.cacassiano.workout_tracker.entities;

import java.time.LocalDateTime;
import java.util.Set;

import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReferenceDTO;
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
import lombok.Setter;
import lombok.ToString;

@Table(name = "workouts")
@Entity(name = "workout")
@Getter
@Setter
@NoArgsConstructor
@ToString
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

    public Workout(Workout w){
        this.id = w.getId();
        this.title = w.getTitle();
        this.scheduleType = w.getScheduleType();
        this.scheduledDate = w.getScheduledDate();
        this.completed = w.getCompleted();
        this.exercises = w.getExercises();
        this.user = w.getUser();
    }

    public Workout(WorkoutReferenceDTO ref, User user) {
        this.title = ref.getTitle();
        this.scheduleType = ref.getSchedule_type();
        this.scheduledDate = ref.getScheduled_date();
        this.completed = ref.getCompleted();
        this.user = user;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
