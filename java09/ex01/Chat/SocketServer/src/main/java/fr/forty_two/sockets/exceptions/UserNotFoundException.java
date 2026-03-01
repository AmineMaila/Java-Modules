package fr.forty_two.sockets.exceptions;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException() {
        super("user not found");
    }

    public UserNotFoundException(String username) {
        super("user '" + username + "' does not exist");
    }
}
