package com.examsystem.models;



import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false)
    private int difficulty; // 1-легкий, 2-средний, 3-сложный

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Teacher createdBy;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketQuestion> ticketQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionScore> questionScores = new ArrayList<>();

    // Constructors
    public Question() {
        this.createdAt = LocalDateTime.now();
    }


    public Question(Subject subject, Topic topic, String text, String type, int difficulty, String answer, Teacher createdBy) {
        this();
        this.subject = subject;
        this.topic = topic;
        this.text = text;
        this.type = type;
        this.difficulty = difficulty;
        this.answer = answer;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Teacher getCreatedBy() { return createdBy; }
    public void setCreatedBy(Teacher createdBy) { this.createdBy = createdBy; }

    public List<TicketQuestion> getTicketQuestions() { return ticketQuestions; }
    public void setTicketQuestions(List<TicketQuestion> ticketQuestions) { this.ticketQuestions = ticketQuestions; }

    public List<QuestionScore> getQuestionScores() { return questionScores; }
    public void setQuestionScores(List<QuestionScore> questionScores) { this.questionScores = questionScores; }

    // Helper methods
    public String getDifficultyString() {
        switch (difficulty) {
            case 1: return "Легкий";
            case 2: return "Средний";
            case 3: return "Сложный";
            default: return "Неизвестно";
        }
    }
}