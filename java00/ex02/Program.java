import java.util.Scanner;

public class Program {
    private static long sumOfDigit(long num) {
        long res = 0;

        while (num > 0) {
            res += num % 10;
            num /= 10;
        }
        return res;
    }

    private static boolean isPrime(long num) {
        if (num == 2) {
            return true;
        }

        if (num <= 1 || num % 2 == 0) {
            return false;
        }
        
        int div = 3;
        long limit = num / 2;
        while (div < limit) {
            if (num % div == 0) {
                return false;
            }
            div+=2;
        }
        return true;
    }
    
    public static void main(String[] args) {
        long queries = 0;

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLong()) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }
            long query = sc.nextLong();
            if (query <= 1) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }
            if (query == 42) {
                break;
            }

            if (isPrime(sumOfDigit(query))) {
                queries++;
            }
        }

        System.out.println("Count of coffee-request : " + queries);
        sc.close();
    }
}
