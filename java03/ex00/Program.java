
public class Program {
    private static final String ARGUMENT_PREFIX = "--count=";

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith(ARGUMENT_PREFIX)) {
            System.err.println("usage java Program --count=<n>");
            return;
        }
        final int disputeCount;
        try {
            disputeCount = Integer.parseInt(args[0].substring(ARGUMENT_PREFIX.length()));
        } catch (NumberFormatException e) {
            System.err.println("input a valid thread count");
            return;
        }

        new Thread(() -> {
            for (int i = 0; i < disputeCount; i++) {
                System.out.println("Egg");
            }
        }).start();
        new Thread(() -> {
                for (int i = 0; i < disputeCount; i++) {
                    System.out.println("Hen");
                }
        }).start();
        for (int i = 0; i < disputeCount; i++) {
            System.out.println("Human");
        }
    }
}
