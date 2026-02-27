package fr.forty_two.sockets.repositories;

import java.util.Optional;

import fr.forty_two.sockets.models.Chatroom;

public interface ChatroomsRepository extends CrudRepository<Chatroom> {
    Optional<Chatroom> findByName(String name);
}
