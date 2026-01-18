import java.util.Scanner;

public class Program {
    public static int[] freqMap(String input) {
        int[] freq = new int[65535];
        char[] charArr = input.toCharArray();

        for (int i = 0; i < charArr.length; i++)
            freq[charArr[i]]++;
        return freq;
    }

    public static char[] maxTen(int[] freq) {
        char[] top = new char[]{0,0,0,0,0,0,0,0,0,0};

        for (int i = 0; i < 65535; i++) {
            if (freq[i] > freq[top[9]]) {
                int j;
                for (j = 8; j >= 0 && freq[i] > freq[top[j]]; j--) {
                    top[j + 1] = top[j];
                }
                top[j + 1] = (char)i;
            }
        }
        return top;
    }

    public static void printGraph(int[] frequency, char[] topTen) {
        float mult = (float)frequency[topTen[0]] / 10;
        
        System.out.printf("%3d%n", frequency[topTen[0]]);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int height = (int)(frequency[topTen[j]] / mult);
                if (height == 10 - i - 1) {
                    System.out.printf("%3d", frequency[topTen[j]]);
                } else if (height >= 10 - i) {
                    System.out.printf("%3c", '#');
                }
            }
            System.out.println();
        }

        for (int j = 0; j < 10; j++) {
            System.out.printf("%3c", topTen[j]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String input = sc.nextLine();

            int[] frequency = freqMap(input);
            char[] topTen = maxTen(frequency);
            printGraph(frequency, topTen);
        }
    }
}
