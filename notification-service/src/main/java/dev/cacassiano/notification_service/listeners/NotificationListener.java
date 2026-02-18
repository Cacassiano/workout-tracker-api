package dev.cacassiano.notification_service.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.cacassiano.notification_service.DTOs.NotificationMessage;
import dev.cacassiano.notification_service.services.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationListener {
    
    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "workout-notification-topic")
    public void sendEmail(NotificationMessage mes) {
        log.info("Received " + mes.users().size() + " users");
        // sending notification
        mes.users().forEach(u -> notificationService.sendNotification(u));
        log.info("Message Processing finished");

    }        
    
}
