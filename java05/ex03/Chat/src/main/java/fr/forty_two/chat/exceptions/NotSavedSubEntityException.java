package fr.forty_two.chat.exceptions;

public class NotSavedSubEntityException extends RuntimeException {
    public NotSavedSubEntityException(String msg) {
        super(msg);
    }
}
