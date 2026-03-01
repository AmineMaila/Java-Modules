package fr.forty_two.sockets.repositories;

import java.util.List;

import fr.forty_two.sockets.models.Message;

public interface MessagesRepository extends CrudRepository<Message> {
    List<Message> findAllByRoomId(Long roomId);
    List<Message> findAllByRoomId(int page, int size, Long roomId);
}
