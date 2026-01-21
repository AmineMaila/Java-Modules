import java.util.UUID;

public class TransactionsService {
    private final UsersList users = new UsersArrayList(); 

    public TransactionsService() {

    }

    public User addUser(String name, double balance) {
        User newUser = new User(name, balance);
        users.add(newUser);
        return newUser;
    }

    public double getUserBalance(int id) {
        return users.findById(id).getBalance();
    }

    public Transaction[] getUserTransfers(int id) {
        return users.findById(id).getTransactionsList().toArray();
    }

    public Transaction removeUserTransaction(int userId, UUID transactionId) {
        return users.findById(userId).getTransactionsList().removeById(transactionId);
    }

    public void transfer(int fromId, int toId, double amount) {
        
        if (amount <= 0) {
            throw new IllegalTransactionException("invalid transfer amount");
        }

        User sender = users.findById(fromId);
        User recipient = users.findById(toId);
        
        if (amount > sender.getBalance() || amount <= 0)
            throw new IllegalTransactionException("Insufficient funds");
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);
        UUID transactionId = UUID.randomUUID();
        Transaction debit = new Transaction(transactionId, sender, recipient, Transaction.TransferCategory.DEBIT, -amount);
        Transaction credit = new Transaction(transactionId, sender, recipient, Transaction.TransferCategory.CREDIT, amount);
        sender.getTransactionsList().add(debit);
        recipient.getTransactionsList().add(credit);
    }

    public Transaction[] checkValidity() {
        TransactionsList result = new TransactionsLinkedList();
        int totalSize = 0;
        for (int i = 0; i < users.size(); i++) {
            totalSize += users.get(i).getTransactionsList().toArray().length;
        }

        Transaction[] allTx = new Transaction[totalSize];
        int allTxIndex = 0;
        for (int i = 0; i < users.size(); i++) {
            Transaction[] userTransactions = users.get(i).getTransactionsList().toArray();
            for (Transaction tx: userTransactions) {
                allTx[allTxIndex++] = tx;
            }
        }
        
        for (int i = 0; i < allTx.length; i++) {
            boolean paired = false;
            for (int j = 0; j < allTx.length; j++) {
                if (i == j) {
                    continue;
                }
                if (allTx[i].getId().equals(allTx[j].getId())
                    && allTx[i].getTransferType() != allTx[j].getTransferType()) {
                    paired = true;
                    break;
                }
            }
            if (!paired) {
                result.add(allTx[i]);
            }
        }

        return result.toArray();
    }
}
