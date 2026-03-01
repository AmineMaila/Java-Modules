package fr.forty_two.sockets.dto.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerResponseDTO {
    private final String status;   // "OK" | "ERROR"
    private final String message;

    @JsonCreator
    public ServerResponseDTO(
        @JsonProperty(value = "status", required = true) String status,
        @JsonProperty(value = "message", required = true) String message
    ) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() { return message; }
    public String getStatus() { return status; }
}
