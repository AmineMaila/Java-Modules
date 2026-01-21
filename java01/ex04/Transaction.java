
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final User recipient;
    private final User sender;
    private final TransferCategory transferType;
    private double amount;

    public enum TransferCategory {
        CREDIT,
        DEBIT
    }
    
    public Transaction(User sender, User recipient, TransferCategory transferType, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.transferType = transferType;
        this.id = UUID.randomUUID();
        
        setAmount(amount);
    }

    public Transaction(UUID transactionId, User sender, User recipient, TransferCategory transferType, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.transferType = transferType;
        this.id = transactionId;
        
        setAmount(amount);
    }

    public UUID getId() {
        return this.id;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public User getSender() {
        return this.sender;
    }

    public TransferCategory getTransferType() {
        return this.transferType;
    }

    public double getAmount() {
        return this.amount;
    }

    public double setAmount(double amount) {
        if ((transferType == TransferCategory.CREDIT && amount < 0)
            || (transferType == TransferCategory.DEBIT && amount > 0)) {
            System.err.println("Error: transaction ammount doesn't match transaction type");
            this.amount = 0;
            return -1;
        }
        this.amount = amount;
        return this.amount;
    }

    @Override
    public String toString() {
        return "Transaction[ID: %s, sender: %s, recipient: %s, type: %s, amount: %f]"
            .formatted(id, sender, recipient, transferType, amount);
    }
}
