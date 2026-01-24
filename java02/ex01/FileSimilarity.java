
import java.util.Map;

public class FileSimilarity {

    private FileSimilarity() {}

    static public double calculate(Map<String, Long> mapA, Map<String, Long> mapB) {
        double numerator = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        double denominator = 0.0;

        for (var entry : mapA.entrySet()) {
            Long bwordFreq = mapB.get(entry.getKey());
            Long awordFreq = entry.getValue();
            if (bwordFreq != null) {
                numerator += bwordFreq * awordFreq;
            }
            normA += awordFreq * awordFreq;
        }

        for (var entry : mapB.entrySet()) {
            Long bwordFreq = entry.getValue();
            normB += bwordFreq * bwordFreq;
        }
        denominator = Math.sqrt(normA) * Math.sqrt(normB);
        return denominator == 0.0 ? 0.0 : numerator / denominator;
    }
}
