package fr.forty_two.sockets.services;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import fr.forty_two.sockets.models.Chatroom;
import fr.forty_two.sockets.repositories.ChatroomsRepository;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final ChatroomsRepository repo;
    private final RoomManager roomManager;

    public RoomsServiceImpl(ChatroomsRepository repo, RoomManager roomManager) {
        this.repo = repo;
        this.roomManager = roomManager;
    }

    @Override
    public Chatroom createRoom(String name) {
        try {
            Chatroom room = new Chatroom(null, name);
            this.repo.save(room);
            return room;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Invalid input");
        } catch (DataAccessException e) {
            throw new IllegalStateException("something went wrong");
        }
    }

    public List<Chatroom> getAllRooms() {
        return repo.findAll();
    }

    public RoomInstance addUser(long roomId, PrintWriter writer) {
        RoomInstance room = roomManager.getOrLoad(roomId);
        room.join(writer);
        return room;
    }


}
