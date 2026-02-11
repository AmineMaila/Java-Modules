package fr.forty_two.chat.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {
    @EqualsAndHashCode.Include
    private Long id;

    private User author;
    private Chatroom room;
    private String message;
    private LocalDateTime created_at;

    @Override
    public String toString() {
        return """
        {
        id=%d,
        author=%s,
        room=%s,
        text="%s",
        dateTime=%s
        }
        """.formatted(id, author, room, message, created_at);
    }
}