import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class Program {
    private static String FOLDER_PREFIX = "--current-folder=";

    public static Path parsePath(String path) throws InvalidPathException {
        Path result = Path.of(path).toAbsolutePath().normalize();

        if (!Files.isDirectory(result)) {
            throw new InvalidPathException(path, "path does not exist or is not a directory");
        }
        return result;
    }
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith(FOLDER_PREFIX)) {
            System.err.println("ARGUMENT ERROR: try --current-folder=<path>");
            return;
        }

        String pStr = args[0].substring(FOLDER_PREFIX.length());
        Path p;
        try {
            p = parsePath(pStr);
        } catch (InvalidPathException e) {
            System.err.println("ARGUMENT ERROR: " + e.getReason());
            return;
        }

        FileManager fm = new FileManager(p);

        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split("\\s+");

                switch (tokens[0]) {
                    case "ls":
                        if (tokens.length != 1) {
                            System.err.println("ls: too many arguments");
                            break;
                        }
                        fm.list();
                        break;
                    case "cd":
                        if (tokens.length != 2) {
                            System.err.println("cd: invalid argument count");
                            break;
                        }
                        fm.changeDir(tokens[1]);
                        break;
                    case "mv":
                        if (tokens.length != 3) {
                            System.err.println("mv: invalid argument count");
                            break;
                        }
                        fm.move(tokens[1], tokens[2]);
                        break;
                    case "exit":
                        return;
                    default:
                        System.err.println("Unknown command");
                }
            }

        }
    }    
}
