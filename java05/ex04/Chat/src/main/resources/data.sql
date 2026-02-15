
-- we insert users
INSERT INTO users (username, password)
VALUES
    ('mohamed', '1'),
    ('amine', '12'),
    ('maila', '123'),
    ('omar', '1234'),
    ('taha', '12345');

/*
    this statement creates 5 chatrooms with the owner mohamed
*/
WITH mohamed AS (
    SELECT id as mohamed_id
    FROM users
    WHERE username = 'mohamed'
)
INSERT INTO chatrooms (name, owner)
SELECT r.name, m.mohamed_id
FROM mohamed m
CROSS JOIN (
    VALUES 
    ('general'),
    ('random'),
    ('study'),
    ('announcements'),
    ('pandora')
) AS r(name);



-- this statement insert every user to every chatroom
INSERT INTO users_chatrooms (user_id, chatroom_id)
SELECT u.id, c.id
FROM users u
CROSS JOIN chatrooms c;

-- this statement inserts messages
INSERT INTO messages (author, room, content)
SELECT user_id, chatroom_id, 'Hello'
FROM users_chatrooms;
