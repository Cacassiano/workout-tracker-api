package dev.cacassiano.notification_service.DTOs;

import java.io.Serializable;
import java.util.List;

// TODO create a list of dtos with just email and workout names
public record NotificationMessage(List<User> users)  implements Serializable {
    
}
