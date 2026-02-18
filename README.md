# CalisTracker

CalisTracker is a comprehensive workout tracking application designed to help users manage their fitness routines, exercises, and receive timely notifications. The application is built using a microservices architecture, leveraging Spring Boot for its services, Kafka for inter-service communication, and Docker Compose for easy deployment.

## Architecture Overview

The CalisTracker application consists of several interconnected services:

- **Workout Service**: The core service for managing user authentication, workout routines, and exercises. See its [README](workout-service/README.md) for more details.
- **Notification Service**: Responsible for sending email notifications based on events from the Workout Service. See its [README](notification-service/README.md) for more details.
- **Kafka**: A distributed streaming platform used for asynchronous communication between services.
- **PostgreSQL**: The primary database for persistent storage.
- **Prometheus & Grafana (Optional)**: For monitoring application metrics.
- **Kafka UI**: A web interface for managing and monitoring Kafka clusters.

## Services

### [Workout Service](workout-service/README.md)

This service is the core logic module responsible for managing user authentication, workout routines, exercises, and scheduled notifications.

**API Documentation:**
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI Docs: `http://localhost:8080/v3/api-docs`

### [Notification Service](notification-service/README.md)

A Spring Boot microservice responsible for sending workout reminders and notifications to users of the Workout Tracker application.

**Configuration:**
The service requires the following environment variables to be set:
- `KAFKA_HOST`: Kafka bootstrap server address
- `EMAIL`: SMTP username (e.g., Gmail address)
- `EMAIL_PASSWORD`: SMTP password or App Password
- `EMAIL_HOST`: SMTP server host
- `EMAIL_PORT`: SMTP server port


**Monitoring:**
- Health: `http://localhost:8081/actuator/health`
- Prometheus Metrics: `http://localhost:8081/actuator/prometheus`

## Setup and Running with Docker Compose

### Prerequisites

- Docker
- Docker Compose

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-repo/calistracker.git
    cd calistracker
    ```

2.  **Configure Environment Variables for Notification Service:**
    Before running, you need to set up the email configuration for the `notification-service`:

    ```yaml
        environment:
          - EMAIL=<Your email>
          - EMAIL_PASSWORD=<your password>
          - EMAIL_HOST=<your email host> # e.g., smtp.gmail.com
          - EMAIL_PORT=<your email host port> # e.g., 587
    ```
    For Gmail, you might need to generate an App Password if you have 2-Factor Authentication enabled.

3.  **Build and Run with Docker Compose:**
    ```bash
    docker-compose up --build
    ```
## Monitoring

The project includes Prometheus for collecting metrics from the Spring Boot applications.

- **Prometheus Dashboard**: Access the Prometheus UI at `http://localhost:9090`
- **Scraped Targets**: The `prometheus/prometheus.yml` configures Prometheus to scrape metrics from:
    - Workout Service: `host.docker.internal:8080/actuator/prometheus`
    - Notification Service: `host.docker.internal:8081/actuator/prometheus`

## Kafka UI

To monitor and manage your Kafka topics and messages, you can use Kafka UI:

- **Kafka UI Dashboard**: Access the Kafka UI at `http://localhost:3000`

## Database

A PostgreSQL database is used for data persistence. You can connect to it using the following details:

- **Host**: `localhost` (from outside Docker) or `db` (from inside Docker)
- **Port**: `5432`
- **Database**: `workoutdb`
- **User**: `postgres`
- **Password**: `postgres`
