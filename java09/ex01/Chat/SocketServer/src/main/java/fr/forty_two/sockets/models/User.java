package fr.forty_two.sockets.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String hashedPassword;
    private final List<Message> messages = new ArrayList<Message>();
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User u = (User) obj;
        if (this.id != null && u.id != null) {
            return this.id.equals(u.id);
        }
        return this.username.equals(u.username);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : username.hashCode();
    }

    @Override
    public String toString() {
        return String.format("{id = %s, username = %s, hashedPassword = %s}", id, username, hashedPassword);
    }
}
