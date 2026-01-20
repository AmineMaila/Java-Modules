import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        long storage = 0;
        int weekCount = 0;
        long mult = 1;
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            if (weekCount > 18) {
                System.err.println("IllegalArgument");
                sc.close();
                System.exit(-1);
            }

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
                if (grade < min) {
                    min = grade;
                }
            }
            sc.nextLine();
            storage += (long)(mult * min);
            weekCount++;
            mult *= 10;
        }
        for (int i = 0; i < weekCount; i++) {
            System.out.print("Week " + (i + 1) + " ");
            int lowest = (int)(storage % 10);
            for (int j = 0; j < lowest; j++) {
                System.out.print('=');
            }
            System.out.println(">");
            storage /= 10;
        }
        sc.close();
    }
}
