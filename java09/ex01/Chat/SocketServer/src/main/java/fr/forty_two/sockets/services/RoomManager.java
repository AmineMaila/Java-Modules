package fr.forty_two.sockets.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import fr.forty_two.sockets.exceptions.RoomNotFoundException;
import fr.forty_two.sockets.models.Chatroom;
import fr.forty_two.sockets.repositories.ChatroomsRepository;

@Service
public class RoomManager {
    private final ChatroomsRepository repo;
    private final Map<Long, RoomInstance> activeRooms = new HashMap<>();

    public RoomManager(ChatroomsRepository repo) {
        this.repo = repo;
    }

    public void createRoom(String name) {
        try {
            this.repo.save(new Chatroom(null, name));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Invalid input");
        } catch (DataAccessException e) {
            throw new IllegalStateException("something went wrong");
        }
    }

    public RoomInstance getOrLoad(Long roomId) {
        return activeRooms.computeIfAbsent(roomId, id -> {
            Optional<Chatroom> result = this.repo.findById(id);
            if (result.isPresent()) {
                return new RoomInstance(result.get().getId());
            }
            throw new RoomNotFoundException("room not found");
        });
    }

    public void removeUser(Long roomId, PrintWriter writer) {

    }

    public void addUser(Long roomId, PrintWriter writer) {

    }

    public void broadcast(String message) {

    }

}
