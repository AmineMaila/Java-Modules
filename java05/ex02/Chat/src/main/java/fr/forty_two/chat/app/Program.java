package fr.forty_two.chat.app;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.zaxxer.hikari.HikariDataSource;

import fr.forty_two.chat.models.Chatroom;
import fr.forty_two.chat.models.Message;
import fr.forty_two.chat.models.User;
import fr.forty_two.chat.repositories.MessagesRepositoryJdbcImpl;

class Program {
    
    public static void main(String[] args) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/42chat");
        ds.setUsername("mmaila");
        ds.setPassword("1234");

        User author = new User(3L, "user", "user", new ArrayList<>(), new ArrayList<>());
        User creator = author;
        Chatroom room = new Chatroom(3L, "room", creator, new ArrayList<>());
        Message message = new Message(null, author, room, "Test!", LocalDateTime.now());
        MessagesRepositoryJdbcImpl repo = new MessagesRepositoryJdbcImpl(ds);
        repo.save(message);
        System.out.println(message.getId()); // ex. id == 11
        ds.close();
    }
}