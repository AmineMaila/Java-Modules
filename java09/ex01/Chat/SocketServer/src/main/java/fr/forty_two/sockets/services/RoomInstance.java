package fr.forty_two.sockets.services;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomInstance {
    private final List<PrintWriter> writers = new CopyOnWriteArrayList<>();
    private final Long id;

    public RoomInstance(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void join(PrintWriter writer) {
        writers.add(writer);
    }

    public void leave(PrintWriter writer) {
        writers.remove(writer);
    }

    public void broadcast(String message) {
        writers.forEach(w -> w.println(message));
    }

    public boolean isEmpty() {
        return writers.isEmpty();
    }
}
