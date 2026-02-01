package fr.forty_two.chat.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@ToString(exclude = {"password", "createdRooms", "chatRooms"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    private Long id;
    
    private String username;
    private String password;
    private List<Chatroom> createdRooms;
    private List<Chatroom> chatRooms;
}
