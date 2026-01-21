public class Program {
    public static void main(String[] args) {
        User alice = new User(1, "Alice", 500.0);
        User bob = new User(2, "Bob", 200.0);

        System.out.println("Before transaction:");
        System.out.println(alice);
        System.out.println(bob);

        Transaction t1 = new Transaction(
                alice,
                bob,
                TransferCategory.DEBIT,
                -150.0
        );

        System.out.println(t1);

        System.out.println(alice);
        System.out.println(bob);
    }
}
