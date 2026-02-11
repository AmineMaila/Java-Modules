package fr.forty_two.chat.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import com.zaxxer.hikari.HikariDataSource;

import fr.forty_two.chat.models.Chatroom;
import fr.forty_two.chat.models.Message;
import fr.forty_two.chat.models.User;
import fr.forty_two.chat.repositories.MessagesRepository;
import fr.forty_two.chat.repositories.MessagesRepositoryJdbcImpl;

class Program {
    
    public static void main(String[] args) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/42chat");
        ds.setUsername("mmaila");
        ds.setPassword("1234");

        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(ds);
        Optional<Message> messageOptional = messagesRepository.findById(11L);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessage("Bye");
            message.setCreated_at(null);
            messagesRepository.update(message);
            var updatedMsg = messagesRepository.findById(11L);
            if (updatedMsg.isPresent()) {
                System.out.println(updatedMsg.get());
            }
        }

        ds.close();
    }
}