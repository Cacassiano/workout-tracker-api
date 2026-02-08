package dev.cacassiano.workout_tracker.use_cases.schedulers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WorkoutScheduler {

    @Autowired
    private UserRepository userRepository; 
    private final int FETCH_LIMIT = 1000;

    @Async
    @Scheduled(cron = "0 54 20 * * ?")
    public void notifyWorkouts() {
        LocalDate today = LocalDate.now();
        int weekDay = today.getDayOfWeek().getValue();
        // Stores the offset of the fetched users
        int offset = 0;
        // Stores the condition for fetch the users
        // Initiate as true for the first patch
        boolean fetchUsers = true;
        // Initiate the arrays before the loop for better performance
        List<User> users;

        log.info("Starting users fetch to notification");
        while (fetchUsers) {
            users = userRepository.findAllByPendingNotificationWorkout(weekDay, today, FETCH_LIMIT, offset);
            if (users.isEmpty()) {
                break;
            }
            int numUsers = users.size();
            offset += numUsers;
            if (numUsers < FETCH_LIMIT) {
                fetchUsers = false;
            }
            // Publising in notification topic
            log.info("Sending {} users for the kafka notification-topic", numUsers);
            users.forEach(e -> System.out.println("email:" + e.getEmail()));

            // Clears the arrays (Reduce garbage collector trash)
            users.clear();
        }
        log.info("{} users processeds", offset+1);
    }

}
