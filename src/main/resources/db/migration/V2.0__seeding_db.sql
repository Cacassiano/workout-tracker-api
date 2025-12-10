-- Inserindo 20 exercícios (inserir primeiro para evitar NOT NULL constraints em outras tabelas)
INSERT INTO exercises (title, repetitions, series) VALUES ('Supino Reto', 12, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Agachamento Livre', 15, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Rosca Direta', 10, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Leg Press', 20, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Desenvolvimento de Ombro', 12, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Rosca Francesa', 12, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Barra Fixa', 8, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Flexão de Braço', 15, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Crucifixo', 12, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Encolhimento de Ombro', 15, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Puxada na Polia', 12, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Extensão de Perna', 15, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Rosca Inversa', 12, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Abdominal', 20, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Prancha', 60, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Stiff', 12, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Elevação Lateral', 15, 3);
INSERT INTO exercises (title, repetitions, series) VALUES ('Supino Inclinado', 10, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Remada Alta', 12, 4);
INSERT INTO exercises (title, repetitions, series) VALUES ('Corrida na Esteira', 30, 1);

-- Inserindo 10 workouts (após os exercícios)
INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino de Peito', '2025-12-08 07:00:00', 'WEEKLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino de Costas', '2025-12-09 07:00:00', 'WEEKLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino de Pernas', '2025-12-10 07:00:00', 'WEEKLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino de Ombro', '2025-12-11 07:00:00', 'DAYLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino de Braço', '2025-12-12 07:00:00', 'DAYLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino de Core', '2025-12-13 07:00:00', 'DAYLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino Cardio', '2025-12-14 07:00:00', 'MONTHLY', false, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino Full Body A', '2025-12-15 07:00:00', 'ONCE', true, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino Full Body B', '2025-12-16 07:00:00', 'ONCE', true, NOW(), NOW());

INSERT INTO workouts (title, scheduled_date, schedule_type, completed, created_at, updated_at) 
VALUES ('Treino Intenso', '2025-12-17 07:00:00', 'WEEKLY', false, NOW(), NOW());


-- Associando exercícios aos workouts através da tabela de relacionamento
-- Observação: o nome da tabela de relacionamento pode variar conforme o mapeamento JPA.
-- Aqui usamos `workout_exercises` com colunas (workout_id, exercise_id).
-- Se a sua aplicação usar outro nome (por ex. `exercises` como join table), ajuste abaixo.

-- Treino de Peito (workout_id = 1): Supino Reto, Crucifixo, Supino Inclinado
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (1, 1);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (1, 9);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (1, 18);

-- Treino de Costas (workout_id = 2): Barra Fixa, Puxada na Polia, Remada Alta
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (2, 7);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (2, 11);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (2, 19);

-- Treino de Pernas (workout_id = 3): Agachamento Livre, Leg Press, Extensão de Perna, Stiff
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (3, 2);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (3, 4);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (3, 12);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (3, 16);

-- Treino de Ombro (workout_id = 4): Desenvolvimento de Ombro, Encolhimento de Ombro, Elevação Lateral
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (4, 5);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (4, 10);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (4, 17);

-- Treino de Braço (workout_id = 5): Rosca Direta, Rosca Francesa, Rosca Inversa
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (5, 3);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (5, 6);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (5, 13);

-- Treino de Core (workout_id = 6): Abdominal, Prancha
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (6, 14);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (6, 15);

-- Treino Cardio (workout_id = 7): Corrida na Esteira, Flexão de Braço
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (7, 20);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (7, 8);

-- Treino Full Body A (workout_id = 8): Agachamento Livre, Supino Reto, Barra Fixa, Desenvolvimento de Ombro
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (8, 2);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (8, 1);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (8, 7);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (8, 5);

-- Treino Full Body B (workout_id = 9): Leg Press, Crucifixo, Puxada na Polia, Rosca Direta
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (9, 4);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (9, 9);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (9, 11);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (9, 3);

-- Treino Intenso (workout_id = 10): Supino Reto, Agachamento Livre, Barra Fixa, Abdominal, Prancha
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (10, 1);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (10, 2);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (10, 7);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (10, 14);
INSERT INTO workout_exercises (workout_id, exercise_id) VALUES (10, 15);