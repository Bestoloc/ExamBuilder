package com.example.ticketbuilder.model;

public class AnalysisRow {

    private Integer questionId;
    private String questionText;
    private Long usedCount;
    private Double averageScore;
    private String comment;

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
