import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class Program {
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
                Set<String> dictionary = new HashSet<>(mapA.keySet());
                dictionary.addAll(mapB.keySet());
                for (String word: dictionary) {
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
