package fr.forty_two.sockets.server;

import org.springframework.stereotype.Component;

import fr.forty_two.sockets.services.UsersService;

@Component
public class Server {
    private final UsersService svc;

    public Server(UsersService svc) {
        this.svc = svc;
    }

    public void run() {
        while (true) {
            
        }
    }
}
