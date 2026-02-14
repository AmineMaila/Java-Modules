package fr.forty_two.repositories;

import fr.forty_two.models.User;

public interface UsersRepository {
    User findByLogin(String login);
    void update(User user);
}
