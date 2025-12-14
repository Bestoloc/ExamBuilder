package com.example.ticketbuilder.model;

public class AnalysisRow {

    private String text;
    private Long usedCount;
    private Double avgScore;

    public AnalysisRow(String text, Long usedCount, Double avgScore) {
        this.text = text;
        this.usedCount = usedCount;
        this.avgScore = avgScore;
    }

    public String getText() { return text; }
    public Long getUsedCount() { return usedCount; }
    public Double getAvgScore() { return avgScore; }
}
