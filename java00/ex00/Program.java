
public class Program {
    public static void main(String[] args) {
        int number = 479598;
        int res = 0;

        while (number > 0) {
            res += number % 10;
            number /= 10;
        }
        System.out.println(res);
    }
}
