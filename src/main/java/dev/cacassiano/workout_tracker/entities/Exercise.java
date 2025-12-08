package dev.cacassiano.workout_tracker.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "title", unique = false, nullable = false)
    private String title;

    @Column(name = "repetitions", unique = false, nullable = false)
    private Integer reps;

    @Column(name = "series", unique = false, nullable = false)
    private Integer series;

    @ManyToMany(mappedBy = "exercises")
    private Set<Workout> workouts;

}
