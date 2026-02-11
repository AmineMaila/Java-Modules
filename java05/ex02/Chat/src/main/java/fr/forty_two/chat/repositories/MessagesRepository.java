package fr.forty_two.chat.repositories;

import fr.forty_two.chat.models.Message;

import java.util.Optional;

public interface MessagesRepository {
    Optional<Message> findById(Long id);
    void save(Message message);
}
