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
                rs.getString("hashed_password")
            );
        }
    }
    private final UserMapper ROW_MAPPER = new UserMapper();

    private final String SELECT_ALL = "SELECT * FROM users";
    private final String SELECT_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final String SELECT_ID = "SELECT * FROM users WHERE id = ?";
    private final String INSERT = "INSERT INTO users(username, hashed_password) VALUES(?, ?)";
    private final String UPDATE = "UPDATE users SET username = ?, hashed_password = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM users WHERE id = ?";

    public UsersRepositoryImpl(DataSource engine) {
        this.jdbcTemplate = new JdbcTemplate(engine);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_USERNAME, ROW_MAPPER, username));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_ID,
                ROW_MAPPER,
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
            ps.setString(2, entity.getHashedPassword());
            return ps;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update(
            UPDATE,
            entity.getUsername(),
            entity.getHashedPassword(),
            entity.getId()
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
