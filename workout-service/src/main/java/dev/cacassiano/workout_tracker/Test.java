package dev.cacassiano.workout_tracker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import dev.cacassiano.workout_tracker.DTOs.kafka.NotificationMessage;
import dev.cacassiano.workout_tracker.DTOs.kafka.UserNotificationDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Test implements CommandLineRunner{

    @Autowired
    private KafkaTemplate<String, NotificationMessage> template;

    @Override
    public void run(String... args) throws Exception {
        log.warn("Lancando um topic");
        UserNotificationDTO dto = new UserNotificationDTO("flamengo@gmail.com");
        NotificationMessage message = new NotificationMessage(List.of(dto));
        template.send("workout-notification-topic",message).join();
        log.warn("Levantei o tópico");
    }
    
}
