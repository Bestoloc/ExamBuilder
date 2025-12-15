package com.example.ticketbuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_history")
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ticket_id")
    private int ticketId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Integer score;

    @Column(name = "comment")
    private String comment;

    // ✅ РАБОЧИЕ SETTERS
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}


