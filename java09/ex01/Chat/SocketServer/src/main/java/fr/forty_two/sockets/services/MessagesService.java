package fr.forty_two.sockets.services;

import java.util.List;

import fr.forty_two.sockets.models.Message;

public interface MessagesService {    

    List<Message> getLast30(Long roomId);
    void storeMessage(String message, Long fromId, Long roomId);
}
