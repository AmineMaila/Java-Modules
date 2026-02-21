package fr.forty_two.service.models;

public class User {
    private Long id;

    private String email;

    public User() {}

    public User(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
