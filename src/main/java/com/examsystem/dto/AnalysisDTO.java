package com.examsystem.dto;

import javafx.beans.property.*;

public class AnalysisDTO {
    private final IntegerProperty questionId = new SimpleIntegerProperty();
    private final StringProperty questionText = new SimpleStringProperty();
    private final StringProperty topicName = new SimpleStringProperty();  // ← строка!
    private final StringProperty subjectName = new SimpleStringProperty(); // ← строка!
    private final StringProperty difficulty = new SimpleStringProperty();
    private final IntegerProperty usageCount = new SimpleIntegerProperty();
    private final DoubleProperty avgScore = new SimpleDoubleProperty();
    private final DoubleProperty minScore = new SimpleDoubleProperty();
    private final DoubleProperty maxScore = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();

    // Конструктор ИЗ МАССИВА, не из Question!
    public AnalysisDTO(Object[] row) {
        this.questionId.set((Integer) row[0]);
        this.questionText.set((String) row[1]);
        this.topicName.set((String) row[2]);      // ← уже строка из запроса
        this.subjectName.set((String) row[3]);    // ← уже строка из запроса
        this.difficulty.set(getDifficultyString((Integer) row[4]));
        this.usageCount.set(((Long) row[5]).intValue());
        this.avgScore.set(row[6] != null ? (Double) row[6] : 0);
        this.minScore.set(row[7] != null ? (Double) row[7] : 0);
        this.maxScore.set(row[8] != null ? (Double) row[8] : 0);
        this.status.set(calculateStatus(this.avgScore.get()));
    }

    private String getDifficultyString(int difficulty) {
        switch (difficulty) {
            case 1: return "Легкий";
            case 2: return "Средний";
            case 3: return "Сложный";
            default: return "Неизвестно";
        }
    }

    private String calculateStatus(double avgScore) {
        if (avgScore == 0) return "Не использовался";
        if (avgScore >= 80) return "Отлично";
        if (avgScore >= 60) return "Нормально";
        if (avgScore >= 40) return "Требует внимания";
        return "Переписать!";
    }

    // Геттеры...
}

