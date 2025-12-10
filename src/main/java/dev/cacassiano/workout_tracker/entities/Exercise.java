package dev.cacassiano.workout_tracker.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "exercise")
@Table(name = "exercises")
@Getter
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = false, nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "repetitions", unique = false, nullable = false)
    private Integer reps;

    @Column(name = "series", unique = false, nullable = false)
    private Integer series;

    @JsonBackReference
    @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
    private Set<Workout> workouts;

}
