CREATE TABLE exercises (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    title text NOT NULL,
    repetitions integer DEFAULT 0,
    series integer DEFAULT 0
);