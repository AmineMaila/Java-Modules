
public class UserIdsGenerator {
    private static UserIdsGenerator instance = null;
    private int lastGeneratedId = 0;

    public static UserIdsGenerator getInstance() {
        if (instance == null)
            instance = new UserIdsGenerator();
        return instance;
    }

    public int generateId() {
        return ++lastGeneratedId;
    }

    private UserIdsGenerator() {

    }
}
