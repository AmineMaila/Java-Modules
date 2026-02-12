package fr.forty_two.chat.repositories;

import java.util.List;

import fr.forty_two.chat.models.User;

public interface UsersRepository {
    List<User> findAll(int page, int size);
}
