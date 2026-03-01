package fr.forty_two.sockets.services;

import java.io.PrintWriter;
import java.util.List;

import fr.forty_two.sockets.models.Chatroom;

public interface RoomsService {
    Chatroom createRoom(String name);
    List<Chatroom> getAllRooms();
    void broadcast(Long roomId, String messageJSON);
    RoomInstance addUser(Long roomId, PrintWriter writer);
    void removeUser(Long roomId, PrintWriter writer);

}
