package forty_two.spring.service.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import forty_two.spring.service.models.User;

@Component
@Qualifier("jdbc")
public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource engine;

    private final String SELECT_ALL = "SELECT * FROM users";
    private final String SELECT_EMAIL = "SELECT * FROM users WHERE email = ?";
    private final String SELECT_ID = "SELECT * FROM users WHERE id = ?";
    private final String INSERT = "INSERT INTO users(email) VALUES(?)";
    private final String UPDATE = "UPDATE users SET email = ?";
    private final String DELETE = "DELETE FROM users WHERE id = ?";

    @Autowired
    public UsersRepositoryJdbcImpl(@Qualifier("driverManager") DataSource engine) {
        this.engine = engine;
    }

    @Override
    public List<User> findAll() {
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL)) {
            
            List<User> users = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password")
                    ));
                }
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(Long id) {
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ID)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_EMAIL)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password")
                    ));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User entity) {
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getEmail());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User entity) {
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getEmail());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
