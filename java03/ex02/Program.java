
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Program {
    public static final String ARGUMENT_ONE_PREFIX =  "--arraySize=";
    public static final String ARGUMENT_TWO_PREFIX =  "--threadsCount=";

    public static int sum(int[] array, int start, int end) {
        return Arrays.stream(array, start, end).sum();
    }

    public static void main(String[] args) {
        if (args.length != 2 || !args[0].startsWith(ARGUMENT_ONE_PREFIX) || !args[1].startsWith(ARGUMENT_TWO_PREFIX)) {
            System.err.println("usage java Program --arraySize=<n> --threadsCount=<n>");
        }

        Random rand = new Random();
        int arraySize, threadCount;

        try {
            arraySize = Integer.parseInt(args[0].substring(ARGUMENT_ONE_PREFIX.length()));
            threadCount = Integer.parseInt(args[1].substring(ARGUMENT_TWO_PREFIX.length()));
            if (threadCount > arraySize || arraySize > 2_000_000) {
                System.err.println("thread count cannot be greater than array size");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input");
            return;
        }

        int[] array = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            array[i] = rand.nextInt(1000);
        }
        System.out.println("Sum: " + sum(array, 0, arraySize));

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        ExecutorCompletionService<Integer> complete = new ExecutorCompletionService<>(pool);
        int part = arraySize / threadCount;
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i + 1;
            final int start = i * part;
            final int end = (threadId == threadCount) ? arraySize : start + part;

            complete.submit(() ->  {
                int result = sum(array, start, end);
                System.out.printf("Thread %d: from %d to %d sum is %d%n", threadId, start, end, result);
                return result;
            });
        }
        int totalSum = 0;
        for (int i = 0; i < threadCount; i++) {
            try {
                Future<Integer> future = complete.take();
                totalSum += future.get();
            } catch (InterruptedException | ExecutionException e) {}
        }
        System.out.println("Sum by threads: " + totalSum);

        pool.shutdown();
    }
}
