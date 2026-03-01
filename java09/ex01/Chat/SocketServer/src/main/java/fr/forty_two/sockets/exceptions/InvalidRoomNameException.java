package fr.forty_two.sockets.exceptions;

public class InvalidRoomNameException extends RuntimeException {
    public InvalidRoomNameException() {
        super("invalid chatroom name");
    }
    
    public InvalidRoomNameException(String msg) {
        super(msg);
    }
}
