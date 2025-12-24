package dev.cacassiano.workout_tracker.entities;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import dev.cacassiano.workout_tracker.DTOs.exercises.ExerciseReqDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "exercise")
@Table(name = "exercises")
@Getter
@NoArgsConstructor
public class Exercise {

    public Exercise(ExerciseReqDTO req, User user) {
        this.title = req.getTitle();
        this.reps = req.getReps();
        this.series = req.getSeries();
        this.category = req.getCategory();
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = false, nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "category", nullable = false, unique = false)
    private String category;

    @Column(name = "repetitions", unique = false, nullable = true)
    private Integer reps;

    @Column(name = "series", unique = false, nullable = true)
    private Integer series;

    @JsonBackReference
    @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
    private Set<Workout> workouts;

    @Column(name = "created_at", nullable = false, unique = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, unique = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "user_id")
    private User user;

    @PrePersist
    private void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    
}
