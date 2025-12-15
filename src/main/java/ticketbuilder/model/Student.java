package ticketbuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "group_name")
    private String group;

    public int getId() { return id; }

    @Override
    public String toString() {
        return fullName + " (" + group + ")";
    }
}
