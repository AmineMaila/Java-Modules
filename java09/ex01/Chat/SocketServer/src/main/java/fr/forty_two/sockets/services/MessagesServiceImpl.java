package fr.forty_two.sockets.services;

import java.util.List;

import fr.forty_two.sockets.models.Message;

public class MessagesServiceImpl implements MessagesService {
    
    @Override
    public void storeMessage(String message, Long fromId, Long roomId) {
        
    }

    @Override
    public List<Message> getLast30(Long roomId) {
        
        return null;
    }
}
