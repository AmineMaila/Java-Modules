package fr.forty_two.sockets.services;

public interface UsersService {
    void signup(String username, String password);
    void signin(String username, String password);
}
