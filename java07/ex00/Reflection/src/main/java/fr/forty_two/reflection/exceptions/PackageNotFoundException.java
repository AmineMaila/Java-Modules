package fr.forty_two.reflection.exceptions;

public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException() {
    }
    
    public PackageNotFoundException(String msg) {
        super(msg);
    }
}
