import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordFrequency {

    private WordFrequency() {}
    
    static public Map<String, Long> process(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<String, Long> freq = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");

                for (String word : words) {
                    freq.merge(word, 1L, (oldVal, newVal) -> oldVal + newVal);
                }
            }
            return freq;
        }
    }
}
