package dev.cacassiano.notification_service.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.cacassiano.notification_service.DTOs.NotificationMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationListener {
    
    @KafkaListener(topics = "workout-notification-topic")
    public void sendEmail(NotificationMessage in) {
        log.info("Received " + in.users().size() + " users");
        in.users().forEach(u -> log.info(u.email()));
    }        
    
}
