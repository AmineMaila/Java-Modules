package fr.forty_two.chat.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"author", "room"})
public class Message {
    @EqualsAndHashCode.Include
    private Long id;

    private User author;
    private Chatroom room;
    private String message;
    private LocalDateTime created_at;
}
