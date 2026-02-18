# Workout Service

This service is the core logic module responsible for managing user authentication, workout routines, exercises, and scheduled notifications.

## Technologies Used

-   **Framework:** Spring Boot (Java 17, Maven)
-   **Database:** PostgreSQL (production), H2 Database (development/testing), Spring Data JPA
-   **Database Migrations:** Flyway
-   **Security:** Spring Security, JWT (JSON Web Tokens) for authentication and authorization
-   **API Documentation:** Springdoc OpenAPI (Swagger UI)
-   **Messaging:** Apache Kafka (for scheduled notifications)
-   **Validation:** Spring Validation
-   **Utilities:** Project Lombok (for reducing boilerplate code)
-   **Monitoring:** Spring Boot Actuator, Prometheus (for metrics collection)
-   **Scheduling:** Spring's `@Scheduled` and `@EnableAsync` for asynchronous task execution
-   **Testing and Quality Assurance**: Junit (for unit tests) and Mockito (for integration tests)

## Swagger endpoints
-   **GET** `/swagger-ui/index.html` 
-   **GET** `/v3/api-docs`

## Key Features

-   **User Management:** Register new users and authenticate existing ones using JWT.
-   **Workout Management:** Create, read, update, and delete workout routines.
-   **Exercise Management:** Perform CRUD operations on exercises, linked to workouts.
-   **Workout Status:** Update the completion status of workouts.
-   **Pagination:** Efficiently retrieve lists of workouts and exercises with pagination support.
-   **Scheduled Notifications:** Automated system for sending notifications about upcoming or pending workouts via Kafka.
-   **OpenAPI/Swagger**: fully compressive documentation of all endpoints
-   **Test Coverage**: +88% of test coverage at all codebase