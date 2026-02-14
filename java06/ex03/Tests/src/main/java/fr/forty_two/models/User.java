package fr.forty_two.models;

import java.util.Objects;

public class User {
    private Long id;
    private String username;
    private String password;
    private Boolean isAuthenticated;

    public User(Long id, String username, String password, Boolean isAuthenticated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User p = (User)obj;
        return Objects.equals(this.id, p.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{id=%d, username=%s, password=%s, isAuthenticated=%s}".formatted(this.id, this.username, this.password, this.isAuthenticated);
    }
}
