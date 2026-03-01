package fr.forty_two.sockets.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.jspecify.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.forty_two.sockets.models.Message;

public class MessagesRepositoryImpl implements MessagesRepository {
    private final JdbcTemplate jdbcTemplate;
    private class MessageMapper implements RowMapper<Message> {
        @Override
        public @Nullable Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Message(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getLong("author"),
                rs.getLong("room")
            );
        }
    }
    private final MessageMapper ROW_MAPPER = new MessageMapper();

    private final String SELECT_ALL = "SELECT * FROM messages LIMIT 1000";
    private final String SELECT_ALL_ROOMID = "SELECT * FROM messages WHERE room = ?";
    private final String SELECT_ALL_ROOMID_PAGE = "SELECT * FROM messages WHERE room = ? LIMIT ? OFFSET ?";
    private final String SELECT_ID = "SELECT * FROM messages WHERE id = ?";
    private final String INSERT = "INSERT INTO messages(content, author, room) VALUES(?, ?, ?)";
    private final String UPDATE = "UPDATE messages SET content = ?, author = ?, room = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM messages WHERE id = ?";

    public MessagesRepositoryImpl(DataSource engine) {
        this.jdbcTemplate = new JdbcTemplate(engine);
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);
    }

    @Override
    public List<Message> findAllByRoomId(Long roomId) {
        return jdbcTemplate.query(SELECT_ALL_ROOMID, ROW_MAPPER, roomId);
    }
    
    @Override
    public List<Message> findAllByRoomId(int page, int size, Long roomId) {
        int offset = page * size;
        int limit = size;
        return jdbcTemplate.query(
            SELECT_ALL_ROOMID_PAGE,
            ROW_MAPPER,
            roomId,
            limit,
            offset
        );
    }

    @Override
    public Optional<Message> findById(Long id) {
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
    public void save(Message entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, entity.getContent());
            ps.setLong(2, entity.getAuthorId());
            ps.setLong(3, entity.getRoomId());
            return ps;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update(
            UPDATE,
            entity.getContent(),
            entity.getAuthorId(),
            entity.getRoomId(),
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
