package com.examsystem.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    void testScoreValidation() {
        // Проверка валидации оценок 2.0-5.0
        assertTrue(isValidScore(2.0));
        assertTrue(isValidScore(3.5));
        assertTrue(isValidScore(5.0));
        assertTrue(isValidScore(4.0));

        assertFalse(isValidScore(1.9));
        assertFalse(isValidScore(5.1));
        assertFalse(isValidScore(0.0));
        assertFalse(isValidScore(6.0));

        // Граничные значения
        assertTrue(isValidScore(2.0), "Минимальная оценка 2.0 должна быть валидной");
        assertTrue(isValidScore(5.0), "Максимальная оценка 5.0 должна быть валидной");
    }

    @Test
    void testDifficultyValidation() {
        // Проверка валидации сложности 1-3
        assertTrue(isValidDifficulty(1));
        assertTrue(isValidDifficulty(2));
        assertTrue(isValidDifficulty(3));

        assertFalse(isValidDifficulty(0));
        assertFalse(isValidDifficulty(4));
        assertFalse(isValidDifficulty(-1));
    }

    private boolean isValidScore(Double score) {
        return score != null && score >= 2.0 && score <= 5.0;
    }

    private boolean isValidDifficulty(Integer difficulty) {
        return difficulty != null && difficulty >= 1 && difficulty <= 3;
    }

    @Test
    void testStringValidation() {
        assertFalse(isEmptyOrNull("Текст"));
        assertFalse(isEmptyOrNull(" A "));

        assertTrue(isEmptyOrNull(""));
        assertTrue(isEmptyOrNull("   "));
        assertTrue(isEmptyOrNull(null));
    }

    private boolean isEmptyOrNull(String text) {
        return text == null || text.trim().isEmpty();
    }
}