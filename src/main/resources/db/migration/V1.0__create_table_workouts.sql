CREATE TABLE workouts(
    id bigint PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
    title text NOT NULL,
    schedule_type varchar(7) NOT NULL,
    scheduled_date TIMESTAMP(6) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

