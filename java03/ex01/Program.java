

public class Program {
    private static final String ARGUMENT_PREFIX = "--count=";
    private static volatile boolean turnEgg = true;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith(ARGUMENT_PREFIX)) {
            System.err.println("usage java Program --count=<n>");
            return;
        }
        final int disputeCount;
        try {
            disputeCount = Integer.parseInt(args[0].substring(ARGUMENT_PREFIX.length()));
        } catch (NumberFormatException e) {
            System.err.println("input a valid count");
            return;
        }
        Thread egg = new Thread("Egg") {
            @Override
            public void run() {
                try {
                    for (int i = 0 ;i < disputeCount; i++) {
                        synchronized (lock) {
                            if (!turnEgg) {
                                lock.wait();
                            }
                            System.out.println("Egg");
                            turnEgg = false;
                            lock.notify();
                        }
                    }
                } catch (InterruptedException e) {}
            }
        };
        egg.start();

        Thread Hen = new Thread("Hen") {
            @Override
            public void run() {
                try {
                    for (int i = 0 ;i < disputeCount; i++) {
                        synchronized (lock) {
                            if (turnEgg) {
                                lock.wait();
                            }
                            System.out.println("Hen");
                            turnEgg = true;
                            lock.notify();
                        }
                    }
                } catch (InterruptedException e) {}
            }
        };
        Hen.start();
    }
}