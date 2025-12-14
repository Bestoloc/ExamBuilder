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

CREATE TABLE usages (
  id SERIAL PRIMARY KEY,
  ticket_id INT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
  date DATE NOT NULL,
  group_name TEXT,
  avg_score NUMERIC
);

-- Sample data
INSERT INTO subjects (name) VALUES ('Mathematics'), ('Physics');

INSERT INTO topics (subject_id, name) VALUES
  (1, 'Algebra'),
  (1, 'Calculus'),
  (2, 'Mechanics'),
  (2, 'Thermodynamics');

INSERT INTO questions (subject_id, topic_id, text, type, difficulty, answer) VALUES
  (1, 1, 'Solve the equation x^2 - 5x + 6 = 0', 'text', 1, 'x=2 or x=3'),
  (1, 2, 'Compute the derivative of sin(x)', 'text', 1, 'cos(x)'),
  (1, 2, 'Evaluate the integral of x dx from 0 to 1', 'text', 1, '1/2'),
  (2, 3, 'State Newton''s second law', 'text', 1, 'F=ma'),
  (2, 4, 'Define entropy qualitatively', 'text', 2, 'Measure of disorder');

-- Create a couple of tickets
INSERT INTO tickets (subject_id, number) VALUES (1, 1), (2, 1);

-- Assign questions to tickets
INSERT INTO ticket_questions (ticket_id, question_id, order_num) VALUES
  (1, 1, 1),
  (1, 2, 2),
  (1, 3, 3),
  (2, 4, 1),
  (2, 5, 2);

-- Sample usages
INSERT INTO usages (ticket_id, date, group_name, avg_score) VALUES
  (1, CURRENT_DATE - INTERVAL '7 days', 'Group A', 3.5),
  (2, CURRENT_DATE - INTERVAL '1 days', 'Group B', 4.0);
