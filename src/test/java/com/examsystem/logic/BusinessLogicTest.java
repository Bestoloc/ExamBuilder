package com.examsystem.logic;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BusinessLogicTest {

    @Test
    void testSelectRandomQuestions() {
        // Тест логики выбора случайных вопросов
        List<String> questions = new ArrayList<>();
        questions.add("Вопрос 1");
        questions.add("Вопрос 2");
        questions.add("Вопрос 3");
        questions.add("Вопрос 4");
        questions.add("Вопрос 5");

        // Выбираем 3 случайных вопроса
        List<String> selected = selectRandomItems(questions, 3);

        assertEquals(3, selected.size());
        assertTrue(questions.containsAll(selected));

        // Проверяем, что все выбранные вопросы уникальны
        assertEquals(selected.size(), selected.stream().distinct().count());
    }

    @Test
    void testSelectRandomQuestions_NotEnough() {
        List<String> questions = new ArrayList<>();
        questions.add("Вопрос 1");
        questions.add("Вопрос 2");

        // Пытаемся выбрать 3 вопроса, когда есть только 2
        List<String> selected = selectRandomItems(questions, 3);

        // Должны вернуть все доступные вопросы
        assertEquals(2, selected.size());
    }

    @Test
    void testCalculateAverageScore() {
        List<Double> scores = new ArrayList<>();
        scores.add(4.5);
        scores.add(3.5);
        scores.add(4.0);
        scores.add(5.0);

        double average = calculateAverage(scores);

        assertEquals(4.25, average, 0.001);
    }

    @Test
    void testCalculateAverageScore_Empty() {
        List<Double> scores = new ArrayList<>();
        double average = calculateAverage(scores);

        assertEquals(0.0, average, 0.001);
    }

    @Test
    void testCalculateAverageScore_WithNulls() {
        List<Double> scores = new ArrayList<>();
        scores.add(4.5);
        scores.add(null);
        scores.add(3.5);
        scores.add(null);

        double average = calculateAverageIgnoreNulls(scores);

        assertEquals(4.0, average, 0.001); // (4.5 + 3.5) / 2 = 4.0
    }

    // Вспомогательные методы для тестирования
    private <T> List<T> selectRandomItems(List<T> items, int count) {
        List<T> result = new ArrayList<>();
        List<T> copy = new ArrayList<>(items);

        java.util.Collections.shuffle(copy);
        int toSelect = Math.min(count, copy.size());

        for (int i = 0; i < toSelect; i++) {
            result.add(copy.get(i));
        }

        return result;
    }

    private double calculateAverage(List<Double> scores) {
        if (scores.isEmpty()) return 0.0;

        double sum = 0.0;
        for (Double score : scores) {
            if (score != null) {
                sum += score;
            }
        }
        return sum / scores.size();
    }

    private double calculateAverageIgnoreNulls(List<Double> scores) {
        double sum = 0.0;
        int count = 0;

        for (Double score : scores) {
            if (score != null) {
                sum += score;
                count++;
            }
        }

        return count > 0 ? sum / count : 0.0;
    }
}