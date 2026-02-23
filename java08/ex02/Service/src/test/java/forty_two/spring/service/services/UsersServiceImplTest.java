package forty_two.spring.service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import forty_two.spring.service.config.TestApplicationConfig;
import forty_two.spring.service.models.User;
import forty_two.spring.service.repositories.UsersRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
public class UsersServiceImplTest {
    
    @Autowired
    @Qualifier("jdbcTemplate")
    private UsersRepository repo;
    
    @Autowired
    private UsersService svc;

    @ParameterizedTest
    @ValueSource(strings = {
        "john@doe.com",
        "jane@doe.ma"
    })
    void shouldReturnGeneratedPassword(String email) {
        String password = svc.signUp(email);
        assertNotNull(password);
        Optional<User> result = repo.findByEmail(email);
        assertTrue(result.isPresent());
        User user = result.get();
        assertEquals(password, user.getPassword());
    }
}
