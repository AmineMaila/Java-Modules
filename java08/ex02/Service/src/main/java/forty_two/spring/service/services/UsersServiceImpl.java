package forty_two.spring.service.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import forty_two.spring.service.models.User;
import forty_two.spring.service.repositories.UsersRepository;

@Component
public class UsersServiceImpl implements UsersService {
    
    private final UsersRepository repo;

    @Autowired
    public UsersServiceImpl(@Qualifier("jdbcTemplate") UsersRepository repo) {
        this.repo = repo;
    }

    @Override
    public String signUp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("invalid email");
        }

        String generatedPassword = UUID.randomUUID().toString();
        try {
            repo.save(new User(
                null,
                email,
                generatedPassword
            ));
            return generatedPassword;
        } catch (DataAccessException e) {
            System.err.println("failed to signup: " + e.getMessage());
            return null;
        }
    }

}
