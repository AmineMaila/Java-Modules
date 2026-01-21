
public class User {
	private final int identifier;
	private String name;
	private long balance;

	public User(int identifier, String name, long balance) {
		this.name = name;
		this.identifier = identifier;
		setBalance(balance);
	}

	public long setBalance(long balance) {
		if (balance < 0) {
			System.out.println("User balance cannot be negative");
			return -1;
		}
		this.balance = balance;
		return this.balance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBalance() {
		return balance;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return identifier;
	}

	@Override
    public String toString() {
        return "User[ID: %d, Name: %s, Balance: %d]".formatted(identifier, name, balance);
    }
}
