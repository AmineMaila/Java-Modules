package fr.forty_two.sockets.models;

import java.time.OffsetDateTime;

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
    private User author;
    private OffsetDateTime created_at;
}
