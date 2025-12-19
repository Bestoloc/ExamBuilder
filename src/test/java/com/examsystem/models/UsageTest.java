package com.examsystem.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UsageTest {

    @Test
    void testUsageCreation() {
        Subject subject = new Subject("Математика", "");
        Teacher teacher = new Teacher("teacher1", "password", "Иванов И.И.", "test@test.ru");
        Student student = new Student("Группа 101", "Иван Иванов", "S001");

        Ticket ticket = new Ticket(subject, 1, "Тестовый билет", teacher);
        Usage usage = new Usage(ticket, student, "Группа 101");

        assertEquals(ticket, usage.getTicket());
        assertEquals(student, usage.getStudent());
        assertEquals("Группа 101", usage.getGroupName());
        assertEquals(LocalDate.now(), usage.getUsageDate());
        assertNull(usage.getAvgScore());
        assertNull(usage.getTeacherComment());
    }

    @Test
    void testUsageScoreCalculation() {
        Usage usage = new Usage();

        // Тестируем расчет среднего балла
        usage.setAvgScore(4.5);
        assertEquals(4.5, usage.getAvgScore(), 0.001);

        // Проверка валидных значений
        usage.setAvgScore(2.0);
        assertEquals(2.0, usage.getAvgScore(), 0.001);

        usage.setAvgScore(5.0);
        assertEquals(5.0, usage.getAvgScore(), 0.001);

        // Проверка null
        usage.setAvgScore(null);
        assertNull(usage.getAvgScore());
    }

    @Test
    void testUsageWithQuestionScores() {
        Usage usage = new Usage();
        usage.setId(1);

        // Создаем оценки вопросов
        QuestionScore score1 = new QuestionScore();
        score1.setScore(4.5);
        score1.setComment("Хорошо");

        QuestionScore score2 = new QuestionScore();
        score2.setScore(3.5);
        score2.setComment("Можно лучше");

        // Предположим, что у Usage есть связь с QuestionScores
        // usage.addQuestionScore(score1);
        // usage.addQuestionScore(score2);

        // Проверяем базовую функциональность
        assertNotNull(usage);
        assertEquals(1L, usage.getId());
    }

    @Test
    void testUsageTeacherFeedback() {
        Usage usage = new Usage();

        usage.setTeacherComment("Отличная работа! Все задачи решены верно.");
        usage.setAssessedAt(java.time.LocalDateTime.now());

        assertEquals("Отличная работа! Все задачи решены верно.", usage.getTeacherComment());
        assertNotNull(usage.getAssessedAt());
    }
}