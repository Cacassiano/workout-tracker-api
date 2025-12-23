CREATE TABLE IF NOT EXISTS users (
    id text PRIMARY KEY,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    email text NOT NULL UNIQUE,
    role VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE workouts
ADD COLUMN user_id text;

ALTER TABLE workouts
ADD CONSTRAINT fk_user
    FOREIGN KEY(user_id) 
        REFERENCES users (id);

ALTER TABLE exercises
ADD COLUMN user_id text;

ALTER TABLE exercises
ADD CONSTRAINT fk_user
    FOREIGN KEY(user_id) 
        REFERENCES users (id);

