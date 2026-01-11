package dev.cacassiano.workout_tracker.use_cases.schedulers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.repositories.UserRepository;

@Component
public class WorkoutScheduler {

    @Autowired
    private UserRepository userRepository; 

    @Async
    @Scheduled(cron = "0 0 9 * * ?")
    // TODO batch processing with spring batch
    public void notifyWorkouts() {
        LocalDate today = LocalDate.now();
        int weekDay = today.getDayOfWeek().getValue();
        List<User> users = userRepository.findAllByPendingNotificationWorkout(weekDay, today);

        // Sent event or just notify
        users.forEach(e -> System.out.println("email:" + e.getEmail()));
    }
}
