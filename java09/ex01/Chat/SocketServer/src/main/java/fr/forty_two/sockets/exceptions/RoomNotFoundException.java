package fr.forty_two.sockets.exceptions;

public class RoomNotFoundException extends RuntimeException {
    
    public RoomNotFoundException() {
        super("Room not found");
    }

    public RoomNotFoundException(String msg) {
        super(msg);
    }
}
