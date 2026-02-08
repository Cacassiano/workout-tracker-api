CREATE TABLE IF NOT EXISTS exercises (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    title text NOT NULL,
    category varchar(100) NOT NULL DEFAULT 'cardio',
    repetitions integer DEFAULT 0,
    series integer DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);