CREATE TABLE workout_exercises(
    workout_id bigint NOT NULL,
    exercise_id BIGSERIAL NOT NULL
);

ALTER TABLE IF EXISTS workout_exercises
ADD CONSTRAINT fk_workouts
    FOREIGN KEY (workout_id)
        REFERENCES workouts(id);

ALTER TABLE IF EXISTS workout_exercises
ADD CONSTRAINT fk_exercises
    FOREIGN KEY (exercise_id)
        REFERENCES exercises(id);