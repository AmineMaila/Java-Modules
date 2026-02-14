package fr.forty_two.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.forty_two.exceptions.AlreadyAuthenticatedException;
import fr.forty_two.models.User;
import fr.forty_two.repositories.UsersRepository;

@ExtendWith(MockitoExtension.class)
public class UsersServiceImplTest {
    @Mock
    UsersRepository repo;

    @InjectMocks
    UsersServiceImpl svc;

    @Test
    void authenticate_returnsTrue_whenCredentialsAreValid() {
        User expectedUser = new User(1L, "mmaila", "1234", false);
        when(repo.findByLogin("mmaila"))
            .thenReturn(expectedUser);
        User userUpdate = new User(1L, "mmaila", "1234", true);

        assertTrue(svc.authenticate("mmaila", "1234"));
        verify(repo).update(userUpdate);
    }

    @Test
    void authenticate_returnsFalse_whenUserNotFound() {
        assertFalse(svc.authenticate("mmaila", "1234"));
        verify(repo, never()).update(any());
    }

    @Test
    void authenticate_returnsFalse_whenPasswordIsIncorrect() {
        User expectedUser = new User(1L, "mmaila", "0000", false);
        when(repo.findByLogin("mmaila"))
            .thenReturn(expectedUser);

        assertFalse(svc.authenticate("mmaila", "1234"));
        verify(repo, never()).update(any());
    }

    @Test
    void authenticate_ThrowsAlreadyAuthenticatedException_whenUserAlreadyAuthenticated() {
        User expectedUser = new User(1L, "mmaila", "1234", true);
        when(repo.findByLogin("mmaila"))
            .thenReturn(expectedUser);

        assertThrows(AlreadyAuthenticatedException.class, () -> {
            svc.authenticate("mmaila", "1234");
        });
        verify(repo, never()).update(any());
    }
}
