package fr.forty_two.sockets.client;

import java.io.IOException;

public interface StateFunction {
    ClientState get() throws IOException;
}
