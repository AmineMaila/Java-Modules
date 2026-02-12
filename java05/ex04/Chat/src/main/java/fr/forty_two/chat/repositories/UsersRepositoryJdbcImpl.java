package fr.forty_two.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import fr.forty_two.chat.models.Chatroom;
import fr.forty_two.chat.models.User;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource engine;
    
    public UsersRepositoryJdbcImpl(DataSource engine) {
        this.engine = engine;
    }

    @Override
    public List<User> findAll(int page, int size) {
        try (Connection conn = engine.getConnection()) {
            final String sql = """
                WITH created AS (
                    SELECT
                        u.id AS user_id,
                        ARRAY_AGG(c.id) AS created_chatroom_ids,
                        ARRAY_AGG(c.name) AS created_chatroom_names
                    FROM users u
                    LEFT JOIN chatrooms c ON u.id = c.owner
                    GROUP BY u.id
                ), joined AS (
                    SELECT
                        u.id AS user_id,
                        ARRAY_AGG(j.id) AS joined_chatroom_ids,
                        ARRAY_AGG(j.name) AS joined_chatroom_names
                    FROM users u
                    LEFT JOIN users_chatrooms uc ON u.id = uc.user_id
                    LEFT JOIN chatrooms j ON j.id = uc.chatroom_id
                    GROUP BY u.id
                )
                SELECT
                    u.id,
                    u.username,
                    u.password,

                    created.created_chatroom_ids,
                    created.created_chatroom_names,

                    joined.joined_chatroom_ids,
                    joined.joined_chatroom_names
                FROM users u
                LEFT JOIN created ON u.id = created.user_id
                LEFT JOIN joined ON u.id = joined.user_id
                ORDER BY u.id
                LIMIT ? OFFSET ?""";
            int offset = page * size;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                final List<User> users = new ArrayList<>();
                while (rs.next()) {
                    List<Chatroom> createdChatrooms = new ArrayList<>();
                    Long[] created_ids = (Long[])(rs.getArray("created_chatroom_ids").getArray());
                    String[] created_names = (String[])(rs.getArray("created_chatroom_names").getArray());
                    if (created_ids[0] != null) {
                        for (int i = 0; i < created_ids.length; i++) {
                            Chatroom room = new Chatroom(created_ids[i], created_names[i], null, new ArrayList<>());
                            createdChatrooms.add(room);
                        }
                    }

                    List<Chatroom> joinedChatrooms = new ArrayList<>();
                    Long[] joined_ids = (Long[])(rs.getArray("joined_chatroom_ids").getArray());
                    String[] joined_names = (String[])(rs.getArray("joined_chatroom_names").getArray());
                    if (joined_ids[0] != null) {
                        for (int i = 0; i < joined_ids.length; i++) {
                            Chatroom room = new Chatroom(joined_ids[i], joined_names[i], null, new ArrayList<>());
                            joinedChatrooms.add(room);
                        }
                    }

                    User user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        createdChatrooms,
                        joinedChatrooms
                    );
                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
