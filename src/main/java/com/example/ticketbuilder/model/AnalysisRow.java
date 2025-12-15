package com.example.ticketbuilder.model;

public class AnalysisRow {

    private final Integer questionId;
    private final String questionText;
    private final Long usedCount;
    private final Double averageScore;
    private final String comment;

    public AnalysisRow(Integer questionId,
                       String questionText,
                       Long usedCount,
                       Double averageScore,
                       String comment) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.usedCount = usedCount;
        this.averageScore = averageScore;
        this.comment = comment;
    }

    public Integer getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public Long getUsedCount() { return usedCount; }
    public Double getAverageScore() { return averageScore; }
    public String getComment() { return comment; }
}
