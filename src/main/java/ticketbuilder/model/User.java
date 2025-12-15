package ticketbuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;
    private String password;
    private String role;

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getRole() { return role; }
}
