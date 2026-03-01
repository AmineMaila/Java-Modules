package fr.forty_two.sockets.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import fr.forty_two.sockets.exceptions.InvalidRoomNameException;
import fr.forty_two.sockets.exceptions.RoomNotFoundException;
import fr.forty_two.sockets.models.Chatroom;
import fr.forty_two.sockets.repositories.ChatroomsRepository;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final ChatroomsRepository repo;
    private final Map<Long, RoomInstance> activeRooms = new HashMap<>();
    
    public RoomsServiceImpl(ChatroomsRepository repo) {
        this.repo = repo;
    }

    @Override
    public Chatroom createRoom(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRoomNameException();
        }

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

    public RoomInstance getOrLoad(Long roomId) {
        return activeRooms.computeIfAbsent(roomId, id -> {
            Optional<Chatroom> result = this.repo.findById(id);
            if (result.isPresent()) {
                return new RoomInstance(result.get().getId());
            }
            throw new RoomNotFoundException("room not found");
        });
    }

    public RoomInstance addUser(Long roomId, PrintWriter writer) {
        RoomInstance instance = this.getOrLoad(roomId);
        instance.join(writer);
        return instance;
    }

    public void removeUser(Long roomId, PrintWriter writer) {
        RoomInstance instance = activeRooms.get(roomId);
        
        if (instance != null) {
            instance.leave(writer);
            if (instance.isEmpty()) {
                activeRooms.remove(roomId);
            }
        }
    }

    public void broadcast(Long roomId, String messageJSON) {
        RoomInstance instance = activeRooms.get(roomId);
        if (instance != null) {
            instance.broadcast(messageJSON);
        }
    }
}
