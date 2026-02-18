package dev.cacassiano.workout_tracker.DTOs.kafka;

import dev.cacassiano.workout_tracker.entities.User;

public record UserNotificationDTO(
    String email,
    String username
) {
    public UserNotificationDTO(User user) {
        this(user.getEmail(), user.getUsername());
    }
}
