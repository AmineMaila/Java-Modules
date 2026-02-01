package fr.forty_two.chat.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = {"messages", "owner"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom {
    @EqualsAndHashCode.Include
    private Long id;
    
    private String name;
    private User owner;
    private List<Message> messages;
}
