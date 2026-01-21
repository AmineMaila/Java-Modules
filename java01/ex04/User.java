
public class User {
	private final int identifier;
	private String name;
	private double balance;
	private final TransactionsList transactions = new TransactionsLinkedList();

	public User(String name, double balance) {
		this.name = name;
		this.identifier = UserIdsGenerator.getInstance().generateId();
		setBalance(balance);
	}

	public double setBalance(double balance) {
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

	public double getBalance() {
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
        return "User[ID: %d, Name: %s, Balance: %f]".formatted(identifier, name, balance);
    }
}
