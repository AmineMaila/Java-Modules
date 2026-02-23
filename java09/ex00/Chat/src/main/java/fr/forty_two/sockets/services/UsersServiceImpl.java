package fr.forty_two.sockets.services;

import org.springframework.stereotype.Service;

import fr.forty_two.sockets.repositories.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {
    
    private final UsersRepository repo;

    public UsersServiceImpl(UsersRepository repo) {
        this.repo = repo;
    }

    @Override
    public void signup(String username, String password) {
        
    }
}
