package com.examsystem.models;


import javax.persistence.*;

@Entity
@Table(name = "question_scores")
public class QuestionScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usage_id", nullable = false)
    private Usage usage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(precision = 4, scale = 2)
    private Double score;

    @Column(columnDefinition = "TEXT")
    private String comment;

    // Constructors
    public QuestionScore() {}

    public QuestionScore(Usage usage, Question question, Double score, String comment) {
        this.usage = usage;
        this.question = question;
        this.score = score;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usage getUsage() { return usage; }
    public void setUsage(Usage usage) { this.usage = usage; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}