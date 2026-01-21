
import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        
        User user1 = new User("mohamed", 20);
        User user2 = new User("amine", 10);
        User user3 = new User("maila", 30);
        User user4 = new User("omar", 40);

        Transaction transaction1 = new Transaction(user1, user2, Transaction.TransferCategory.CREDIT, 10);
        Transaction transaction2 = new Transaction(user2, user3, Transaction.TransferCategory.CREDIT, 4);
        Transaction transaction3 = new Transaction(user3, user4, Transaction.TransferCategory.DEBIT, -4);

        TransactionsList transactionsList = new TransactionsLinkedList();
        transactionsList.add(transaction1);
        transactionsList.add(transaction2);
        transactionsList.add(transaction3);
        
        Transaction[] transactions = transactionsList.toArray();
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        UUID removedId = transaction3.getId();
        transactionsList.removeById(transaction3.getId());

        try {
            transactionsList.removeById(removedId);
        } catch (TransactionNotFoundException e) {
            System.out.println("Transaction Not found");
        }
    }
}
