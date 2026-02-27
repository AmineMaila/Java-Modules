package fr.forty_two.sockets.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Chatroom {
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
}
