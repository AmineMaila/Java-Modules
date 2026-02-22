package fr.forty_two.service.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.jspecify.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.forty_two.service.models.User;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;
    private class UserMapper implements RowMapper<User> {
        @Override
        public @Nullable User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                rs.getLong("id"),
                rs.getString("email")
            );
        }
    }

    private final String SELECT_ALL = "SELECT * FROM users";
    private final String SELECT_EMAIL = "SELECT * FROM users WHERE email = ?";
    private final String SELECT_ID = "SELECT * FROM users WHERE id = ?";
    private final String INSERT = "INSERT INTO users(email) VALUES(?)";
    private final String UPDATE = "UPDATE users SET email = ?";
    private final String DELETE = "DELETE FROM users WHERE id = ?";

    public UsersRepositoryJdbcTemplateImpl(DataSource engine) {
        this.jdbcTemplate = new JdbcTemplate(engine);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new UserMapper());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_EMAIL, new UserMapper(), email));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                SELECT_ID,
                new UserMapper(),
                id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, entity.getEmail());
            return ps;
        }, keyHolder);

        entity.setId((Long)keyHolder.getKey());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update(
            UPDATE,
            entity.getEmail()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
            DELETE,
            id
        );
    }
}
