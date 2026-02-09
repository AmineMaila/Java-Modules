package fr.forty_two.chat.app;

import java.util.Optional;
import java.util.Scanner;

import com.zaxxer.hikari.HikariDataSource;

import fr.forty_two.chat.models.Message;
import fr.forty_two.chat.repositories.MessagesRepositoryJdbcImpl;

class Program {
    
    public static void main(String[] args) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/42chat");
        ds.setUsername("mmaila");
        ds.setPassword("1234");

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter a message ID");
            if (sc.hasNextLong()) {
                Long id = sc.nextLong();
                MessagesRepositoryJdbcImpl repo = new MessagesRepositoryJdbcImpl(ds);
                Optional<Message> msg = repo.findById(id);
                msg.ifPresentOrElse(
                    value -> System.out.println("Message : " + value),
                    () -> System.err.println("Message not found")
                );
            } else {
                System.err.println("Invalid Input");
            }
        }
        ds.close();
    }
}