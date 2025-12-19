package com.examsystem.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String email;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> createdQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> createdTickets = new ArrayList<>();

    @OneToMany(mappedBy = "assessedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usage> assessedUsages = new ArrayList<>();

    // Constructors
    public Teacher() {}

    public Teacher(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Question> getCreatedQuestions() { return createdQuestions; }
    public void setCreatedQuestions(List<Question> createdQuestions) { this.createdQuestions = createdQuestions; }

    public List<Ticket> getCreatedTickets() { return createdTickets; }
    public void setCreatedTickets(List<Ticket> createdTickets) { this.createdTickets = createdTickets; }

    public List<Usage> getAssessedUsages() { return assessedUsages; }
    public void setAssessedUsages(List<Usage> assessedUsages) { this.assessedUsages = assessedUsages; }
}