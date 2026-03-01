package fr.forty_two.sockets.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {

    @EqualsAndHashCode.Include
    private Long id;

    private String content;
    private Long authorId;
    private Long roomId;

    public Message(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
