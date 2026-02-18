package dev.cacassiano.notification_service.services;

import dev.cacassiano.notification_service.DTOs.User;

public interface NotificationService {
    void sendNotification(User user);
}
