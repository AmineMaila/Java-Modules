package fr.forty_two.sockets.repositories;

import java.util.Optional;

import fr.forty_two.sockets.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByUsername(String username);
}
