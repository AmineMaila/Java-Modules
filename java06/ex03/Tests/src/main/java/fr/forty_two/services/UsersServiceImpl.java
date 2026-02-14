package fr.forty_two.services;

import fr.forty_two.exceptions.AlreadyAuthenticatedException;
import fr.forty_two.models.User;
import fr.forty_two.repositories.UsersRepository;

public class UsersServiceImpl {
    private final UsersRepository repository;

    public UsersServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(String login, String password) {
        User user = repository.findByLogin(login);

        if (user == null || !password.equals(user.getPassword())) {
            return false;
        }

        if (user.getIsAuthenticated()) {
            throw new AlreadyAuthenticatedException("User '%s' already authenticated".formatted(login));
        }

        user.setIsAuthenticated(true);
        repository.update(user);
        return true;
    }
}
