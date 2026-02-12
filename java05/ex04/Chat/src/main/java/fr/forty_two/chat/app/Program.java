package fr.forty_two.chat.app;

import java.util.List;

import com.zaxxer.hikari.HikariDataSource;

import fr.forty_two.chat.models.User;
import fr.forty_two.chat.repositories.UsersRepository;
import fr.forty_two.chat.repositories.UsersRepositoryJdbcImpl;

class Program {
    
    public static void main(String[] args) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/42chat");
        ds.setUsername("mmaila");
        ds.setPassword("1234");

        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(ds);
        List<User> users = usersRepository.findAll(0, 5);

        for (var user : users) {
            System.out.println("------------------");
            System.out.println(user);
            System.out.println("********Joined_chat_rooms*********");
            for (var chatroom: user.getChatRooms()) {
                System.out.println(chatroom);
            }
            System.out.println("*****************");
            System.out.println("********Created_chat_rooms*********");
            for (var created_chatroom: user.getCreatedRooms()) {
                System.out.println(created_chatroom);
            }
            System.out.println("*****************");
            System.out.println("------------------");
        }

        ds.close();
    }
}