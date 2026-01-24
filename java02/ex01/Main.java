import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong argument count");
            return;
        }

        try {
            var mapA = WordFrequency.process(args[0]);
            var mapB =  WordFrequency.process(args[1]);
            
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.DOWN);
            System.out.println("Similarity = " + df.format(FileSimilarity.calculate(mapA, mapB)));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("dictionary.txt"))) {
                for (String word: mapA.keySet()) {
                    writer.write(word);
                    writer.newLine();
                }
    
                for (String word: mapB.keySet()) {
                    writer.write(word);
                    writer.newLine();
                }
                writer.flush();
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
