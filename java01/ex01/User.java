
public class User {
	private final int id;
	private final String name;
	private double balance = 0.0;

	public User(String name) {
		this.name = name;
		this.id = UserIdsGenerator.getInstance().generateId();
	}

    @Override
    public String toString() {
        return "User[ID: %d, Name: %s, Balance: %f]".formatted(id, name, balance);
    }

	public double setBalance(double balance) {
		if (balance < 0) {
			return -1;
		}
		this.balance = balance;
		return this.balance;
	}

	public double getBalance() {
		return balance;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}