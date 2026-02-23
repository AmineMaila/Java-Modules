package fr.forty_two.sockets.repositories;

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
import org.springframework.stereotype.Repository;

import fr.forty_two.sockets.models.User;

@Repository
public class UsersRepositoryImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;
    private class UserMapper implements RowMapper<User> {
        @Override
        public @Nullable User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password")
            );
        }
    }

    private final String SELECT_ALL = "SELECT * FROM users";
    private final String SELECT_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final String SELECT_ID = "SELECT * FROM users WHERE id = ?";
    private final String INSERT = "INSERT INTO users(username, \"password\") VALUES(?, ?)";
    private final String UPDATE = "UPDATE users SET username = ?, \"password\" = ?";
    private final String DELETE = "DELETE FROM users WHERE id = ?";

    public UsersRepositoryImpl(DataSource engine) {
        this.jdbcTemplate = new JdbcTemplate(engine);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new UserMapper());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_USERNAME, new UserMapper(), username));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                SELECT_ID,
                new UserMapper(),
                id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            return ps;
        }, keyHolder);

        entity.setId((Long)keyHolder.getKey());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update(
            UPDATE,
            entity.getUsername(),
            entity.getPassword()
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
