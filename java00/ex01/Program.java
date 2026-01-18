import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            if (!sc.hasNextInt()) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }

            int num = sc.nextInt();
            if (num <= 1) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }

            if (num == 2) {
                System.out.println("true 1");
                return;
            }

            if (num % 2 == 0) {
                System.out.println("false 1");
                return;
            }

            int div = 3;
            int steps = 1;
            int limit = num / 2;
            while (div < limit) {
                if (num % div == 0) {
                    System.out.println("false " + steps);
                    return;
                }
                steps++;
                div+=2;
            }
            System.out.println("true " + steps);
        }
    } 
    
}
