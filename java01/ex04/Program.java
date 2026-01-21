public class Program {
    public static void main(String[] args) {
        TransactionsService service = new TransactionsService();

        User mohamed = service.addUser("Mohamed", 300);
        User amine = service.addUser("Amine", 1000);
        User maila   = service.addUser("Maila", 500);

        System.out.println("Initial balances:");
        System.out.println(amine.getName() + ": " + service.getUserBalance(amine.getId()));
        System.out.println(maila.getName() + ": " + service.getUserBalance(maila.getId()));
        System.out.println(mohamed.getName() + ": " + service.getUserBalance(mohamed.getId()));
        System.out.println();

        System.out.println("Performing transfers...");
        service.transfer(amine.getId(), maila.getId(), 200);    // amine -> maila 200
        service.transfer(maila.getId(), mohamed.getId(), 100);  // maila -> mohamed 100
        service.transfer(amine.getId(), mohamed.getId(), 50); // amine -> mohamed 50
        System.out.println();

        System.out.println("Balances after transfers:");
        System.out.println(amine.getName() + ": " + service.getUserBalance(amine.getId()));
        System.out.println(maila.getName() + ": " + service.getUserBalance(maila.getId()));
        System.out.println(mohamed.getName() + ": " + service.getUserBalance(mohamed.getId()));
        System.out.println();
        System.out.println("Transactions for amine:");
        Transaction[] amineTxs = service.getUserTransfers(amine.getId());
        for (Transaction tx : amineTxs) {
            System.out.println(tx);
        }
        System.out.println();

        System.out.println("Transactions for maila:");
        Transaction[] mailaTxs = service.getUserTransfers(maila.getId());
        for (Transaction tx : mailaTxs) {
            System.out.println(tx);
        }
        System.out.println();

        System.out.println("Transactions for mohamed:");
        Transaction[] mohamedTxs = service.getUserTransfers(mohamed.getId());
        for (Transaction tx : mohamedTxs) {
            System.out.println(tx);
        }
        System.out.println();

        System.out.println("Removing amine's first transaction: " + amineTxs[0].getId());
        service.removeUserTransaction(amine.getId(), amineTxs[0].getId());
        System.out.println();

        System.out.println("Removing maila's last transaction: " + mailaTxs[1].getId());
        service.removeUserTransaction(maila.getId(), mailaTxs[1].getId());
        System.out.println();

        System.out.println("Unpaired transactions in the system: ");
        Transaction[] invalidTxs = service.checkValidity();
        for (Transaction tx : invalidTxs) {
            System.out.println(tx);
        }
    }
}
