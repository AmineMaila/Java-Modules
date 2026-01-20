
public class UsersArrayList implements UsersList{
    private User[] users = new User[10];
    private int capacity = 10;
    private int size = 0;
    
    public UsersArrayList() {}

    @Override
    public void add(User user) {
        if (size == capacity) {
            int newCapacity = capacity *= 1.5;
            User[] larger = new User[newCapacity];
            for (int i = 0; i < size; i++) {
                larger[i] = users[i];
            }
            users = larger;
            capacity = newCapacity;
        }

        users[size++] = user;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public User get(int index) {
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
