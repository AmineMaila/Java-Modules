
public class User {
	private final int identifier;
	private String name;
	private long balance;
	private final TransactionsList transactions = new TransactionsLinkedList();

	public User(String name, long balance) {
		this.name = name;
		this.identifier = UserIdsGenerator.getInstance().generateId();
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

	public void addTransaction(Transaction tx) {
		transactions.add(tx);
	}

	public TransactionsList getTransactionsList() {
		return transactions;
	}

	public int getId() {
		return identifier;
	}

	@Override
    public String toString() {
        return "User[ID: %d, Name: %s, Balance: %d]".formatted(identifier, name, balance);
    }
}
