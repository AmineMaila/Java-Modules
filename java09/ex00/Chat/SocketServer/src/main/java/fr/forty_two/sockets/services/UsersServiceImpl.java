package fr.forty_two.sockets.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.forty_two.sockets.models.User;
import fr.forty_two.sockets.repositories.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {
    
    private final UsersRepository repo;
    private final PasswordEncoder encoder;

    public UsersServiceImpl(UsersRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void signup(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("empty username or password");
        }
        repo.findByUsername(username).ifPresentOrElse((user) -> {
            throw new IllegalStateException("user already exists");
        }, () -> {
            String hashedPassword = encoder.encode(password);
            repo.save(new User(null, username, hashedPassword));
        });
    }
}
