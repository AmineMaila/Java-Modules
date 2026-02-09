package fr.forty_two.chat.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Chatroom {
    @EqualsAndHashCode.Include
    private Long id;
    
    private String name;
    private User owner;
    private List<Message> messages;

    @Override
    public String toString() {
        return "{id=%d,name=%s,creator=%s}".formatted(id, name, owner);
    }
}
