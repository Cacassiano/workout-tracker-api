# Notification Service

A Spring Boot microservice responsible for sending workout reminders and notifications to users of the Workout Tracker application.

## Overview

The **Notification Service** acts as a consumer in the modular monolith architecture. It listens for workout-related events and sends automated email reminders to users to help them stay consistent with their fitness goals.

## Technologies Used

- **Java 17**: Core programming language.
- **Spring Boot 4.0.0**: Main framework for the microservice.
- **Spring Kafka**: Used to consume notification messages from the `workout-notification-topic`.
- **Spring Boot Starter Mail**: Integration with SMTP servers to send emails.
- **Spring Boot Actuator**: Provides production-ready features like health checks and metrics.
- **Micrometer Prometheus**: Exposes metrics for monitoring via Prometheus.
- **Lombok**: Reduces boilerplate code (Getters, Setters, Slf4j, etc.).
- **Jackson**: Handles JSON deserialization for Kafka messages.

## Key Features

- **Kafka Consumer**: Listens to the `workout-notification-topic` for incoming notification requests.
- **Email Reminders**: Sends personalized emails to users about pending workouts.
- **Error Handling**: Robust error handling for email delivery failures.
- **Monitoring**: Health and metrics endpoints enabled via Actuator.

## Configuration

The service requires the following environment variables to be set:

- `KAFKA_HOST`: Kafka bootstrap server address
- `EMAIL`: SMTP username (e.g., Gmail address)
- `EMAIL_PASSWORD`: SMTP password or App Password 
- `EMAIL_HOST`: SMTP server host 
- `EMAIL_PORT`: SMTP server port 

## Message Format

The service expects a JSON message on Kafka containing a list of users to notify:

```json
{
  "users": [
    {
      "username": "username",
      "email": "email@email.com"
    }
  ]
}
```

## Monitoring

Endpoints available (standard Actuator paths):
- Health: `/actuator/health`
- Prometheus Metrics: `/actuator/prometheus`
