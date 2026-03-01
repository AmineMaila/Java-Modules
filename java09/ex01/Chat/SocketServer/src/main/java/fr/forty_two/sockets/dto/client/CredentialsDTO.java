package fr.forty_two.sockets.dto.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialsDTO extends ClientCommand {
    private final String username;
    private final String password;

    @JsonCreator
    public CredentialsDTO(
        @JsonProperty(value = "type", required = true) String type,
        @JsonProperty(value = "username", required = true) String username,
        @JsonProperty(value = "password", required = true) String password
    ) {
        super(type);
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
