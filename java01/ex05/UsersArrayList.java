
public class UsersArrayList implements UsersList{
    private User[] users = new User[10];
    private int size = 0;

    @Override
    public void add(User user) {
        if (size == users.length) {
            int newCapacity = (int)(users.length * 1.5);
            User[] larger = new User[newCapacity];
            for (int i = 0; i < size; i++) {
                larger[i] = users[i];
            }
            users = larger;
        }

        users[size++] = user;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public User get(int index) {
        if (index < 0 || index >= size) {
            throw new UserNotFoundException();
        }
        return users[index];
    }

    @Override
    public User findById(int id) {
        for (int i = 0; i < size; i++) {
            if (users[i].getId() == id)
                return users[i];
        }
        throw new UserNotFoundException();
    }
}
