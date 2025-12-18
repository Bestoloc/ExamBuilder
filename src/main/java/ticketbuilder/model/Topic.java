package ticketbuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // ===== GETTERS =====
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // ===== SETTERS =====
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // для ComboBox
    @Override
    public String toString() {
        return name;
    }
}
