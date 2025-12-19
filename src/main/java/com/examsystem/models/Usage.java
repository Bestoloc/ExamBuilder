package com.examsystem.models;




import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usages")
public class Usage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "avg_score", precision = 4, scale = 2)
    private Double avgScore;

    @Column(name = "teacher_comment", columnDefinition = "TEXT")
    private String teacherComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessed_by")
    private Teacher assessedBy;

    @Column(name = "assessed_at")
    private LocalDateTime assessedAt;

    @OneToMany(mappedBy = "usage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionScore> questionScores = new ArrayList<>();

    // Constructors
    public Usage() {
        this.usageDate = LocalDate.now();
    }

    public Usage(Ticket ticket, Student student, String groupName) {
        this();
        this.ticket = ticket;
        this.student = student;
        this.groupName = groupName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getUsageDate() { return usageDate; }
    public void setUsageDate(LocalDate usageDate) { this.usageDate = usageDate; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Double getAvgScore() { return avgScore; }
    public void setAvgScore(Double avgScore) { this.avgScore = avgScore; }

    public String getTeacherComment() { return teacherComment; }
    public void setTeacherComment(String teacherComment) { this.teacherComment = teacherComment; }

    public Teacher getAssessedBy() { return assessedBy; }
    public void setAssessedBy(Teacher assessedBy) { this.assessedBy = assessedBy; }

    public LocalDateTime getAssessedAt() { return assessedAt; }
    public void setAssessedAt(LocalDateTime assessedAt) { this.assessedAt = assessedAt; }

    public List<QuestionScore> getQuestionScores() { return questionScores; }
    public void setQuestionScores(List<QuestionScore> questionScores) { this.questionScores = questionScores; }
}