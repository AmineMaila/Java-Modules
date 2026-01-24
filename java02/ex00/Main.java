import java.io.IOException;

public class Main {
    private static final String SIGNATURE_FILE_NAME = "signatures.txt";
    
    public static void main(String[] args) {
        try {
            Signatures signatures = new Signatures();
            signatures.parse(SIGNATURE_FILE_NAME);
            FileIdentifier identifier = new FileIdentifier(signatures);
            identifier.process();
        } catch (Signatures.InvalidFormatException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
