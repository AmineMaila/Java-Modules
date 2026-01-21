public class Program {
    public static void main(String[] args) {
        boolean dev = false;
        if (args.length > 0 && args[0].equals("--profile=dev")) {
            dev = true;
        }
        Menu m = new Menu(dev);

        m.run();
    }
}
