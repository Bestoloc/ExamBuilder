-- exam_schema.sql
-- PostgreSQL schema and test data for "Exam Ticket Builder" project

CREATE TABLE subjects (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE topics (
  id SERIAL PRIMARY KEY,
  subject_id INT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
  name TEXT NOT NULL
);

CREATE TABLE questions (
  id SERIAL PRIMARY KEY,
  subject_id INT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
  topic_id INT NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  type TEXT DEFAULT 'text',
  difficulty INT CHECK (difficulty BETWEEN 1 AND 3),
  answer TEXT
);

CREATE TABLE tickets (
  id SERIAL PRIMARY KEY,
  subject_id INT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
  number INT NOT NULL
);

CREATE TABLE ticket_questions (	
  ticket_id INT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
  question_id INT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
  order_num INT NOT NULL,
  PRIMARY KEY (ticket_id, question_id)
);

create table students(
	id SERIAL PRIMARY KEY,
    full_name VARCHAR(50) UNIQUE NOT null,
    group_name varchar(10)
);

CREATE TABLE ticket_history (
    id SERIAL PRIMARY KEY,
    ticket_id INT NOT NULL,
    question_id INT NOT NULL,
    student_id int REFERENCES students(id),
    score INT,  -- студент получил за этот вопрос
    created_at TIMESTAMP DEFAULT NOW(),
    comment text
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

INSERT INTO students (full_name, group_name)
VALUES ('Valera', '225'), ('Nikita', '227'), ('Petya', '232');

-- тестовый преподаватель
INSERT INTO users (login, password, role)
VALUES ('teacher', '1', 'TEACHER');

-- Sample data
INSERT INTO subjects (name) VALUES ('Mathematics'), ('Physics');

INSERT INTO topics (subject_id, name) VALUES
  (1, 'Algebra'),
  (1, 'Calculus'),
  (2, 'Mechanics'),
  (2, 'Thermodynamics');

INSERT INTO questions (subject_id, topic_id, text, type, difficulty, answer) VALUES
(1, 1, 'Что такое инкапсуляция?', 1, 1, '…'),
(1, 1, 'Приведите пример наследования.', 1, 2, '…'),
(1, 2, 'Написать сортировку пузырьком.', 2, 2, '…'),
(2, 3, 'Что такое нормализация БД?', 1, 1, '…');

-- Create a couple of tickets
INSERT INTO tickets (subject_id, number) VALUES (1, 1), (2, 1);

-- Assign questions to tickets
INSERT INTO ticket_questions (ticket_id, question_id, order_num) VALUES
  (1, 1, 1),
  (1, 2, 2),
  (1, 3, 3),
  (2, 4, 1),
  (1, 4, 2);
