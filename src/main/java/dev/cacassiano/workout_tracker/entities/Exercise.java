package dev.cacassiano.workout_tracker.entities;

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
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = false, nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "repetitions", unique = false, nullable = true)
    private Integer reps;

    @Column(name = "series", unique = false, nullable = true)
    private Integer series;

    @JsonBackReference
    @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
    private Set<Workout> workouts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "user_id")
    private User user;
}
