import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class FileIdentifier {
    private final Signatures signatures;
    private final FileOutputStream result;

    public FileIdentifier(Signatures signatures) throws FileNotFoundException {
        this.signatures = signatures;
        this.result = new FileOutputStream("result.txt");
    }

    public boolean compareSignatures(byte[] ref, byte[] fileSignature) {
        if (fileSignature.length < ref.length) {
            return false;
        }

        for (int i = 0; i < ref.length && i < fileSignature.length; i++) {
            if (ref[i] != fileSignature[i]) {
                return false;
            }
        }

        return true;
    }

    public void process() {
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("42")) {
                    break;
                }
                try (FileInputStream s = new FileInputStream(line)) {
                    byte[] fileSignature = new byte[signatures.getMaxSignatureSize()];
                    int bytesRead = s.read(fileSignature);
                    if (bytesRead == -1) {
                        continue ;
                    }
                    boolean processed = false;
                    for (Map.Entry<String, byte[]> signature : signatures.entrySet()) {
                        if (compareSignatures(signature.getValue(), fileSignature)) {
                            result.write(signature.getKey().getBytes(StandardCharsets.UTF_8));
                            System.out.println("PROCESSED");
                            processed = true;
                            break;
                        }
                    }
                    if (!processed) {
                        System.out.println("UNDEFINED");
                    }
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }

            }
        }
    }
}
