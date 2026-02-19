package fr.forty_two.orm.exceptions;

public class OrmException extends RuntimeException {
    private Exception og;

    public OrmException(String msg, Exception e) {
        super(msg);
        this.og = e;
    }

    @Override
    public String toString() {
        return super.getMessage() + og;
    }
}
