package dev.cacassiano.workout_tracker.infra.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafkaRetryTopic
public class KafkaConfig {

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name("workout-notification-topic")
            .partitions(1)
            .replicas(1)
            .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
            .build();
    }
}



