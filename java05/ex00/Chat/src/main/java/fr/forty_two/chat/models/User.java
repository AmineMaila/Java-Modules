package fr.forty_two.chat.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    private Long id;
    
    private String username;
    private String password;
    private List<Chatroom> createdRooms;
    private List<Chatroom> chatRooms;

    @Override
    public String toString() {
        return "id=%d,login=%s,password=%s".formatted(id, username);
    }
}
