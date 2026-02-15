package fr.forty_two.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import fr.forty_two.chat.exceptions.NotSavedSubEntityException;
import fr.forty_two.chat.models.Chatroom;
import fr.forty_two.chat.models.Message;
import fr.forty_two.chat.models.User;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final DataSource engine;

    public MessagesRepositoryJdbcImpl(DataSource engine) {
        this.engine = engine;
    }
    
    @Override
    public void save(Message message) {
        try (Connection conn = engine.getConnection();
            PreparedStatement msgPs = conn.prepareStatement("""
                INSERT INTO messages (
                    author,
                    room,
                    content,
                    created_at
                ) VALUES(?, ?, ?, ?)""",PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement userPs = conn.prepareStatement("""
                    SELECT COUNT(*) FROM users WHERE id = ?""");
            PreparedStatement chatroomPs = conn.prepareStatement("""
                    SELECT COUNT(*) FROM chatrooms WHERE id = ?""")) {

            Long authorId = message.getAuthor().getId();
            Long roomId = message.getRoom().getId();

            userPs.setLong(1, authorId);
            chatroomPs.setLong(1, roomId);

            try (var userRs = userPs.executeQuery()) {
                userRs.next();
                if (userRs.getLong(1) == 0) {
                    throw new NotSavedSubEntityException("author of message does not exist");
                }
            }

            try (var chatroomRs = chatroomPs.executeQuery()) {
                chatroomRs.next();
                if (chatroomRs.getLong(1) == 0) {
                    throw new NotSavedSubEntityException("chatroom of message does not exist");
                }
            }

            msgPs.setLong(1, authorId);
            msgPs.setLong(2, roomId);
            msgPs.setString(3, message.getMessage());
            LocalDateTime created_at = message.getCreated_at();
            if (created_at == null) {
                msgPs.setNull(4, Types.TIMESTAMP);
            } else {
                msgPs.setTimestamp(4, Timestamp.valueOf(created_at));
            }
            msgPs.executeUpdate();
            try (ResultSet generatedKeys = msgPs.getGeneratedKeys()) {
                generatedKeys.next();
                message.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
        }
        
    }

    @Override
    public Optional<Message> findById(Long id) {
        final String sql = """
        SELECT
            m.id AS message_id,
            m.content,
            m.created_at,

            u.id AS author_id,
            u.username AS author_username,
            u.password AS author_password,

            c.id AS chatroom_id,
            c.name AS chatroom_name,
            
            o.id AS owner_id,
            o.username AS owner_username,
            o.password AS owner_password
        FROM messages m
        JOIN users u ON m.author = u.id
        JOIN chatrooms c ON m.room = c.id
        JOIN users o ON c.owner = o.id
        WHERE m.id = ?
        """;
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User author = new User(
                        rs.getLong("author_id"),
                        rs.getString("author_username"),
                        rs.getString("author_password"),
                        null,
                        null
                    );

                    User owner = new User(
                        rs.getLong("owner_id"),
                        rs.getString("owner_username"),
                        rs.getString("owner_password"),
                        null,
                        null
                    );

                    Chatroom room = new Chatroom(
                        rs.getLong("chatroom_id"),
                        rs.getString("chatroom_name"),
                        owner,
                        null
                    );

                    Timestamp created_at = rs.getTimestamp("created_at");
                    return Optional.of(new Message(
                        id,
                        author,
                        room,
                        rs.getString("content"),
                        created_at == null ? null : created_at.toLocalDateTime()
                    ));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLERR: " + e);
            return Optional.empty();
        }
    }
}
