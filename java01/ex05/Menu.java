import java.util.Scanner;
import java.util.UUID;

public class Menu {
    private final TransactionsService svc = new TransactionsService();
    private final boolean dev;
    private class InvalidInputException extends RuntimeException {
        InvalidInputException() {
            super("invalid input");
        }
    };
    private class UnknownOptionException extends RuntimeException {
        UnknownOptionException() {
            super("unknown option");
        }
    };

    public Menu(boolean dev) {
        this.dev = dev;
    }

    public void printMenu() {
        System.out.println("""
        1. Add a user
        2. View user balances
        3. Perform a transfer
        4. View all transactions for a specific user
        """);
        if (dev) {
            System.out.println("""
            5. DEV - remove a transfer by ID
            6. DEV - check transfer validity
            7. Finish execution
            """);
        } else {
            System.out.println("5. Finish execution");
        }
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            try {
                printMenu();
                if (!sc.hasNextInt()) {
                    throw new InvalidInputException();
                }
                int cmdNum = sc.nextInt();
                String cmdRem = sc.nextLine().trim();
                if (!cmdRem.isEmpty())
                    throw new InvalidInputException();
                switch (cmdNum) {
                    case 1 -> {
                        System.out.println("Enter a user name and a balance");
                        String name = sc.next();
                        if (!sc.hasNextLong()) {
                            throw new InvalidInputException();
                        }
                        long balance = sc.nextLong();
                        String remainder = sc.nextLine().trim();
                        if (!remainder.isEmpty())
                            throw new InvalidInputException();
    
                        User newUser = svc.addUser(name, balance);
                        System.out.println("User with id = " + newUser.getId() + " is added");
                    }
                    case 2 -> {
                        System.out.println("Enter a user ID");
                        if (!sc.hasNextInt()) {
                            throw new InvalidInputException();
                        }
                        int userId = sc.nextInt();
                        String remainder = sc.nextLine().trim();
                        if (!remainder.isEmpty())
                            throw new InvalidInputException();

                        String name = svc.getUserName(userId);
                        long balance = svc.getUserBalance(userId);
                        System.out.println(name + " - " + balance);
                    }
                    case 3 -> {
                        System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
                        if (!sc.hasNextInt()) {
                            throw new InvalidInputException();
                        }
                        int senderId = sc.nextInt();
                        if (!sc.hasNextInt()) {
                            throw new InvalidInputException();
                        }
                        int recipientId = sc.nextInt();

                        if (!sc.hasNextLong()) {
                            throw new InvalidInputException();
                        }
                        long amount = sc.nextLong();

                        String remainder = sc.nextLine().trim();
                        if (!remainder.isEmpty())
                            throw new InvalidInputException();
                        svc.transfer(senderId, recipientId, amount);
                        System.out.println("The transfer is completed");
                    }
                    case 4 -> {
                        System.out.println("Enter a user ID");
                        if (!sc.hasNextInt()) {
                            throw new InvalidInputException();
                        }

                        int userId = sc.nextInt();
                        String remainder = sc.nextLine().trim();
                        if (!remainder.isEmpty())
                            throw new InvalidInputException();

                        Transaction[] txs = svc.getUserTransfers(userId);
                        for (Transaction tx : txs) {
                            if (tx.getTransferType() == Transaction.TransferCategory.DEBIT) {
                                User recipient = tx.getRecipient();
                                System.out.println("To %s(id = %d) %d with id = %s".formatted(recipient.getName(), recipient.getId(), tx.getAmount(), tx.getId()));
                            } else {
                                User sender = tx.getRecipient();
                                System.out.println("From %s(id = %d) %d with id = %s".formatted(sender.getName(), sender.getId(), tx.getAmount(), tx.getId()));
                            }
                        }
                    }
                    case 5 -> {
                        if (!dev) {
                            sc.close();
                            return;
                        }
                        System.out.println("Enter a user ID and a transfer ID");
                        if (!sc.hasNextInt()) {
                            throw new InvalidInputException();
                        }
                        int userId = sc.nextInt();

                        if (!sc.hasNext()) {
                            throw new InvalidInputException();
                        }
                        UUID transferId = UUID.fromString(sc.next());
                        Transaction tx = svc.removeUserTransaction(userId, transferId);
                        if (tx.getTransferType() == Transaction.TransferCategory.DEBIT) {
                            User recipient = tx.getRecipient();
                            System.out.println("Transfer To %s(id = %d) %d removed".formatted(recipient.getName(), recipient.getId(), tx.getAmount()));
                        } else {
                            User sender = tx.getRecipient();
                            System.out.println("Transfer From %s(id = %d) %d removed".formatted(sender.getName(), sender.getId(), tx.getAmount()));
                        }
                    }
                    case 6 -> {
                        if (!dev) {
                            throw new UnknownOptionException();
                        }
                        System.out.println("Check Results:");
                        Transaction[] unpairedTransactions = svc.checkValidity();
                        for () {
                            
                        }
                    }
                    case 7 -> {
                        if (!dev) {
                            throw new UnknownOptionException();
                        }
                        sc.close();
                        return;
                    }
                    default -> throw new UnknownOptionException();
                }
            } catch (RuntimeException ite) {
                System.err.println("Error: " + ite.getMessage());
            }
            System.out.println("---------------------------------------------------------");
        }
        sc.close();
    }
}
