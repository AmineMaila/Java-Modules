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

import fr.forty_two.sockets.models.Chatroom;

@Repository
public class ChatroomsRepositoryImpl implements ChatroomsRepository {
    private final JdbcTemplate jdbcTemplate;
    private class RoomMapper implements RowMapper<Chatroom> {
        @Override
        public @Nullable Chatroom mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Chatroom(
                rs.getLong("id"),
                rs.getString("name")
            );
        }
    }

    private final String SELECT_ALL = "SELECT * FROM chatrooms";
    private final String SELECT_NAME = "SELECT * FROM chatrooms WHERE name = ?";
    private final String SELECT_ID = "SELECT * FROM chatrooms WHERE id = ?";
    private final String INSERT = "INSERT INTO chatrooms(name) VALUES(?)";
    private final String UPDATE = "UPDATE chatrooms SET name = ?";
    private final String DELETE = "DELETE FROM chatrooms WHERE id = ?";

    public ChatroomsRepositoryImpl(DataSource engine) {
        this.jdbcTemplate = new JdbcTemplate(engine);
    }

    @Override
    public List<Chatroom> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new RoomMapper());
    }

    @Override
    public Optional<Chatroom> findByName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_NAME, new RoomMapper(), name));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                SELECT_ID,
                new RoomMapper(),
                id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Chatroom entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, entity.getName());
            return ps;
        }, keyHolder);

        entity.setId((Long)keyHolder.getKey());
    }

    @Override
    public void update(Chatroom entity) {
        jdbcTemplate.update(
            UPDATE,
            entity.getName()
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
