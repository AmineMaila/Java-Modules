package fr.forty_two.sockets.exceptions;

public class AuthException extends RuntimeException {
    public AuthException() {}
    
    public AuthException(String msg) {
        super(msg);
    }
}
