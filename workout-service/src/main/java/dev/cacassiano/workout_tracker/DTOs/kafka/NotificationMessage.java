package dev.cacassiano.workout_tracker.DTOs.kafka;

import java.io.Serializable;
import java.util.List;

public record NotificationMessage(List<UserNotificationDTO> users)  implements Serializable {
    
}
