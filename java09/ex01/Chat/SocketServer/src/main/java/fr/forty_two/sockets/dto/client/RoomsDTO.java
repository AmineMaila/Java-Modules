package fr.forty_two.sockets.dto.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomsDTO extends ClientCommand {
    private final String name;

    @JsonCreator
    public RoomsDTO(
        @JsonProperty(value = "type", required = true) String type,
        @JsonProperty("name") String name
    ) {
        super(type);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
