package forty_two.spring.service.repositories;

import java.util.Optional;

import forty_two.spring.service.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);

}
