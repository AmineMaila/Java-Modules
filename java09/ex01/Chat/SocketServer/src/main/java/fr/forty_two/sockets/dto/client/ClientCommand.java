package fr.forty_two.sockets.dto.client;

public abstract class ClientCommand {
    private final String type;

    public ClientCommand(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
