import java.util.Scanner;

public class Program {
    public static int[] freqMap(String input) {
        int[] freq = new int[65535]; // already lexographic order
        char[] charArr = input.toCharArray();

        for (int i = 0; i < charArr.length; i++) {
            freq[charArr[i]]++;
        }
        return freq;
    }

    public static char[] maxTen(int[] freq) {
        char[] top = new char[]{0,0,0,0,0,0,0,0,0,0}; // value at index 9 is lowest

        for (int i = 0; i < 65535; i++) {
            if (freq[i] > freq[top[9]]) {
                int j = 8;
                while (j >= 0 && freq[i] > freq[top[j]]) {
                    top[j + 1] = top[j];
                    j--;
                }
                top[j + 1] = (char)i;
            }
        }
        return top;
    }

    public static void printGraph(int[] frequency, char[] topTen) {
        float mult = (float)frequency[topTen[0]] / 10;
        
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 10; j++) {
                int height = (int)(frequency[topTen[j]] / mult);
                if (height == 10 - i) {
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
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            sc.close();
            return;
        }

        String input = sc.nextLine();

        int[] frequency = freqMap(input);
        char[] topTen = maxTen(frequency);
        printGraph(frequency, topTen);

        sc.close();
    }
}