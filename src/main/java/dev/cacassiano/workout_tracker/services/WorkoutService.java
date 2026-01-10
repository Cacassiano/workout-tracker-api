package dev.cacassiano.workout_tracker.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.workouts.WorkouReferenceDTO;
import dev.cacassiano.workout_tracker.DTOs.workouts.WorkoutReqDTO;
import dev.cacassiano.workout_tracker.entities.Exercise;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.entities.Workout;
import dev.cacassiano.workout_tracker.errors.custom.NotFoundException;
import dev.cacassiano.workout_tracker.repositories.WorkoutRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;

    // UPDATE and INSERT
    public void updateStatus(Boolean completed, Long id, String userId) throws NotFoundException {
        if(workoutRepository.updateStatus(completed, id, userId) == 0 ){
            throw new NotFoundException("Workout not found for id: "+ id);
        }
    }

    @Transactional
    public Workout updateWorkout(WorkoutReqDTO req, Long id, User user, Set<Exercise> exercises){
        

        Workout workout = workoutRepository.getReferenceByIdAndUser(id, user.getId());
        workout.update(req, exercises, null);


        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout saveWorkout(WorkoutReqDTO req, User user, Set<Exercise> exercises) {
        
        Workout workout = new Workout();
        workout.update(req, exercises, user);

        return workoutRepository.save(workout);
    }

    // DELETE
    @Transactional
    public void deleteWorkoutById(Long id, String user_id) throws NotFoundException{
        if(workoutRepository.deleteByIdAndCount(id) == 0){
            throw new NotFoundException("Workout not found for id: "+ id);
        }
    }

    // SELECT
    @Transactional
    public Page<Workout> getAllWorkouts(User user, Boolean withExercises, Pageable pageable) {
        if (withExercises) {
            return workoutRepository.findAllWithExercises(user, pageable);
        }
        return workoutRepository.findAllWorkouts(user, pageable);
    }

    public Workout getReferenceByIdAndUser(Long id, User user) {
        return workoutRepository.getReferenceByIdAndUser(id, user.getId());
    }

    public Set<Workout> filterAndGetWorkoutRefs(Set<WorkouReferenceDTO> refs, User user) {
        Set<Workout> workouts = new HashSet<>();
        if (refs == null || refs.isEmpty()) {
            log.info("returning a empty workout refs set");
            return workouts;
        }
        refs.forEach(e -> {
            if (e.getId() != null) {
                workouts.add( getReferenceByIdAndUser(e.getId(), user) );
                return;
            }
            workouts.add( new Workout(e, user) );
        });
        log.info("returning a {} length workout refs set", workouts.size());
        return workouts;
    }
} 
