package fr.forty_two.service.repositories;

import java.util.Optional;

import fr.forty_two.service.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);

}
