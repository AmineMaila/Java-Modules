
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class IdThreadFactory implements ThreadFactory {
    private int count = 1;

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "Thread-" + count++);
    }
}

public class Program {
    public static final String ARGUMENT_PREFIX =  "--threadsCount=";
    public static final String FILES_URLS =  "files_urls.txt";

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith(ARGUMENT_PREFIX)) {
            System.err.println("usage java Program --threadsCount=<n>");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILES_URLS))) {
            int threadCount;
            threadCount = Integer.parseInt(args[0].substring(ARGUMENT_PREFIX.length()));

            ExecutorService pool = Executors.newFixedThreadPool(threadCount, new IdThreadFactory());
            String url;
            int lineCount = 0;
            while ((url = reader.readLine()) != null) {
                try  {
                    URI uri = new URI(url);
                    int fileId = lineCount + 1;
                    pool.execute(() -> {
                        try (InputStream in = uri.toURL().openStream()) {
                            System.out.printf("%s start download file number %d%n", Thread.currentThread().getName(), fileId);
                            Path dest = Path.of(uri.getPath()).getFileName();
                            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
                            System.out.printf("%s finish download file number %d%n", Thread.currentThread().getName(), fileId);
                        } catch (Exception e) {
                            System.err.println("Download failed for '" + uri + "': " + e);
                        }
                    });
                } catch (URISyntaxException e) {
                    System.err.println("URI syntax: " + e.getMessage());
                }
                lineCount++;
            }
            pool.shutdown();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input");
            return;
        } catch (IOException e) {
            System.err.println("Cannot read urls file: " + e);
            return;
        }
    }
}
