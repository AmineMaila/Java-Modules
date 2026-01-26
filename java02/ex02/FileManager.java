import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class FileManager {
    private Path currentDir;
    public FileManager(Path currentDir) {
        this.currentDir = currentDir;
    }

    public static Long getSize(Path p) {
        if (Files.isRegularFile(p)) {
            try {
                return Files.size(p);
            } catch (IOException e) {
                System.err.println("ls: cannot open file '" + p.getFileName() + "': " + e.getMessage());
                return null;
            }
        }

        long[] totalSize = {0};
        try {
            Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
                    totalSize[0] += attr.size();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
                    System.err.println("ls: cannot open file '" + file.getFileName() + "': " + e.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("ls: cannot open directory '" + p.getFileName() + "': " + e.getMessage());
            return null;
        }
        return totalSize[0];
    }

    public void move(String source, String target) {
        Path srcP = currentDir.resolve(source).normalize();
        System.out.println("Src: " + srcP);
        if (!Files.isRegularFile(srcP)) {
            System.out.println(srcP);
            System.err.println("mv: source operand is not a file or does not exist");
            return;
        }

        Path dstP = currentDir.resolve(target).normalize();
        System.out.println("Dest: " + dstP);
        if (Files.isDirectory(dstP)) {
            dstP = dstP.resolve(srcP.getFileName());
        } else {
            Path dstParent = dstP.getParent();
            if (dstParent != null && Files.notExists(dstParent)) {
                System.err.println("mv: destination path does not exist");
                return;
            }
        }

        try {
            Files.move(srcP, dstP);
        } catch (IOException e) {
            System.err.println("mv: unable to move file: " + e.getMessage());
        }
    }

    public void changeDir(String where) {
        Path newDir = currentDir.resolve(where).normalize();
        if (!Files.isDirectory(newDir)) {
            System.err.println("cd: path does not exist or is not a directory");
            return;
        }
        currentDir = newDir;
        System.out.println(currentDir);
    }

    public void list() {
        try (Stream<Path> root = Files.list(currentDir)) {
            root.forEach(file -> {
                Long size = getSize(file);
                System.out.printf("%s %s%n", file.getFileName(), formatSize(size));
            });
        } catch (IOException e) {
            System.err.println("ls: cannot list directory: " + e.getMessage());
        }
    }

    private String formatSize(Long bytes) {
        if (bytes == null) {
            return "(null)";
        }

        if (bytes < 1024) {
            return String.format("%d B", bytes);
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}
