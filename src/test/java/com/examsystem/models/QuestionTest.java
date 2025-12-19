package com.examsystem.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testQuestionCreation() {
        Question question = new Question();

        question.setText("Решите уравнение: 2x + 5 = 15");
        question.setAnswer("x = 5");
        question.setDifficulty(2);
        question.setType("открытый");
        question.setCreatedAt(LocalDateTime.now());

        assertEquals("Решите уравнение: 2x + 5 = 15", question.getText());
        assertEquals("x = 5", question.getAnswer());
        assertEquals(2, question.getDifficulty());
        assertEquals("открытый", question.getType());
        assertNotNull(question.getCreatedAt());
    }

    @Test
    void testQuestionDifficultyValidation() {
        Question question = new Question();

        // Проверка допустимых значений сложности (1-3)
        question.setDifficulty(1);
        assertEquals(1, question.getDifficulty());
        assertEquals("Легкий", question.getDifficultyString());

        question.setDifficulty(2);
        assertEquals(2, question.getDifficulty());
        assertEquals("Средний", question.getDifficultyString());

        question.setDifficulty(3);
        assertEquals(3, question.getDifficulty());
        assertEquals("Сложный", question.getDifficultyString());
    }

    @Test
    void testQuestionWithTopicAndSubject() {
        // Создаем иерархию: Subject → Topic → Question
        Subject subject = new Subject("Математика", "Основы математики");
        subject.setId(1);

        Topic topic = new Topic(subject, "Алгебра", "Линейные уравнения");
        topic.setId(2);

        Question question = new Question();
        question.setText("Найдите корни уравнения: x² - 5x + 6 = 0");
        question.setTopic(topic);

        assertEquals(topic, question.getTopic());
        assertEquals("Алгебра", question.getTopic().getName());
        assertEquals("Математика", question.getTopic().getSubject().getName());
    }

    @Test
    void testQuestionWithTeacher() {
        Teacher teacher = new Teacher("math_teacher", "pass123", "Сидоров П.П.", "sidorov@edu.ru");
        teacher.setId(3);

        Question question = new Question();
        question.setText("Докажите теорему Пифагора");
        question.setCreatedBy(teacher);

        assertEquals(teacher, question.getCreatedBy());
        assertEquals("Сидоров П.П.", question.getCreatedBy().getFullName());
    }

    @Test
    void testQuestionTypes() {
        Question question = new Question();

        // Проверка различных типов вопросов
        String[] types = {"теоретический", "практический", "задача", "тест"};

        for (String type : types) {
            question.setType(type);
            assertEquals(type, question.getType());
        }
    }
}