import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        long storage = 0;
        int weekCount = 0;
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine() && weekCount < 18) {
            String line = sc.nextLine();
            if (line.equals("42")) {
                break;
            }

            if (!line.equals("Week " + (weekCount + 1))) {
                System.err.println("IllegalArgument");
                sc.close();
                System.exit(-1);
            }

            int min = 9;
            for (int j = 0; j < 5; j++) {
                if (!sc.hasNextInt()) {
                    System.err.println("IllegalArgument");
                    sc.close();
                    System.exit(-1);
                }
                int grade = sc.nextInt();
                if (grade < 1 || grade > 9) {
                    System.err.println("IllegalArgument");
                    sc.close();
                    System.exit(-1);
                }
                min = grade < min ? grade : min;
            }
            sc.nextLine();
            storage = storage * 10 + min;
            weekCount++;
        }

        long divisor = 1;
        for (int i = 1; i < weekCount; i++) {
            divisor *= 10;
        }

        for (int i = weekCount - 1; i >= 0; i--) {
            System.out.print("Week " + (weekCount - i) + " ");

            int lowest = (int)(storage / divisor);
            storage %= divisor;
            divisor /= 10;

            for (int j = 0; j < lowest; j++) {
                System.out.print('=');
            }
            System.out.println(">");
        }
        sc.close();
    }
}