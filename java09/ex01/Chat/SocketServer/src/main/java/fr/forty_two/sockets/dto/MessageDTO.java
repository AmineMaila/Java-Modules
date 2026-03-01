package fr.forty_two.sockets.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {

    private final String message;
    private final String fromUsername;

    @JsonCreator
    public MessageDTO(
        @JsonProperty(value = "content", required = true) String message,
        @JsonProperty(value = "fromUsername", required = true) String fromUsername
    ) {
        this.message = message;
        this.fromUsername = fromUsername;
    }

    public String getMessage() { return message; }
    public String getFromUsername() { return fromUsername; }
}
