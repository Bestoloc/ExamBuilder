package com.examsystem.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "student_code", unique = true, length = 20)
    private String studentCode;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usage> usages = new ArrayList<>();

    // Constructors
    public Student() {}

    public Student(String groupName, String fullName, String studentCode) {
        this.groupName = groupName;
        this.fullName = fullName;
        this.studentCode = studentCode;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public List<Usage> getUsages() { return usages; }
    public void setUsages(List<Usage> usages) { this.usages = usages; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        // Для сущностей JPA обычно сравнивают по ID
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return fullName + " (" + groupName + ")";
    }
}