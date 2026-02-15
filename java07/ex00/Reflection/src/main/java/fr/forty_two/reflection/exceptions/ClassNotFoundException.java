package fr.forty_two.reflection.exceptions;

public class ClassNotFoundException extends IllegalArgumentException {
    public ClassNotFoundException() {
    }
    
    public ClassNotFoundException(String msg) {
        super(msg);
    }
}
