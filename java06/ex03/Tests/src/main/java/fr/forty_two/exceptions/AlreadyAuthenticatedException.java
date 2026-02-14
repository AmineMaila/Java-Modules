package fr.forty_two.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException {
    public AlreadyAuthenticatedException() {}
    
    public AlreadyAuthenticatedException(String msg) {
        super(msg);
    }
}
