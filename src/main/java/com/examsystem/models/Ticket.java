package com.examsystem.models;



import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private int number;

    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Teacher createdBy;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketQuestion> ticketQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usage> usages = new ArrayList<>();

    // Constructors
    public Ticket() {
        this.createdAt = LocalDateTime.now();
    }

    public Ticket(Subject subject, int number, String name, Teacher createdBy) {
        this();
        this.subject = subject;
        this.number = number;
        this.name = name;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Teacher getCreatedBy() { return createdBy; }
    public void setCreatedBy(Teacher createdBy) { this.createdBy = createdBy; }

    public List<TicketQuestion> getTicketQuestions() { return ticketQuestions; }
    public void setTicketQuestions(List<TicketQuestion> ticketQuestions) { this.ticketQuestions = ticketQuestions; }

    public List<Usage> getUsages() { return usages; }
    public void setUsages(List<Usage> usages) { this.usages = usages; }

    // Helper method to get questions
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        for (TicketQuestion tq : ticketQuestions) {
            questions.add(tq.getQuestion());
        }
        return questions;
    }
}