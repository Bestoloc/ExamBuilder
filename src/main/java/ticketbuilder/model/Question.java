package ticketbuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "subject_id")
    private Integer subjectId;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "type_id")
    private Integer typeId;

    private String text;
    private Integer difficulty;
    private String answer;

    // ===== GETTERS =====
    public int getId() { return id; }
    public Integer getSubjectId() { return subjectId; }
    public Topic getTopic() { return topic; }
    public Integer getTypeId() { return typeId; }
    public String getText() { return text; }
    public Integer getDifficulty() { return difficulty; }
    public String getAnswer() { return answer; }

    // ===== SETTERS =====
    public void setId(int id) { this.id = id; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
    public void setTopic(Topic topic) { this.topic = topic; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }
    public void setText(String text) { this.text = text; }
    public void setDifficulty(Integer difficulty) { this.difficulty = difficulty; }
    public void setAnswer(String answer) { this.answer = answer; }
}

