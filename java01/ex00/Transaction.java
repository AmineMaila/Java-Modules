
import java.util.UUID;

enum TransferCategory {
    INCOMING,
    OUTGOING
}

public class Transaction {
    private final UUID id = UUID.randomUUID();
    private final User recipient;
    private final User sender;
    private final TransferCategory transferType;
    private final double amount;
    
    public Transaction(User recipient, User sender, TransferCategory transferType, double amount) {
        this.recipient = recipient;
        this.sender = sender;
        this.transferType = transferType;
        
        if ((transferType == TransferCategory.INCOMING && amount < 0)
            || (transferType == TransferCategory.OUTGOING && amount > 0)) {
            System.err.println("Error: transaction ammount doesn't match transaction type");
            System.exit(-1);
        }
        this.amount = amount;
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
}
