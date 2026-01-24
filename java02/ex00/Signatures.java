import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Signatures {
    private final Map<String, byte[]> signatures = new HashMap<>();
    private int maxSignatureSize = 0;

    public static class InvalidFormatException extends Exception {

        public InvalidFormatException(String line) {
            super("Invalid format in signatures file: '" + line + "'");
        }
    }

    public int getMaxSignatureSize() {
        return maxSignatureSize;
    }

    public Set<Map.Entry<String, byte[]>> entrySet() {
        return signatures.entrySet();
    }

    public void parse(String file) throws InvalidFormatException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\s*,\\s*");
                if (parts.length != 2) {
                    throw new InvalidFormatException(line);
                }

                String[] hexValuesStr = parts[1].split("\\s+");

                byte[] hexValuesB = new byte[hexValuesStr.length];
                for (int i = 0; i < hexValuesStr.length; i++) {
                    try {
                        int val = Integer.parseInt(hexValuesStr[i], 16);
                        if (val > 255)
                            throw new InvalidFormatException(line);
                        hexValuesB[i] = (byte) Integer.parseInt(hexValuesStr[i], 16);
                    } catch (NumberFormatException e) {
                        throw new InvalidFormatException(line);
                    }
                }
                maxSignatureSize = Math.max(hexValuesB.length, maxSignatureSize);
                signatures.put(parts[0], hexValuesB);
            }
            if (signatures.isEmpty()) {

            }
        }
    }
}
