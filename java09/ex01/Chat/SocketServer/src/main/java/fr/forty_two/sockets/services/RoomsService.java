package fr.forty_two.sockets.services;

import java.io.PrintWriter;
import java.util.List;

import fr.forty_two.sockets.models.Chatroom;

public interface RoomsService {
    Chatroom createRoom(String name);
    public List<Chatroom> getAllRooms();
    public RoomInstance addUser(long roomId, PrintWriter writer);
}
