package fr.forty_two.spring.preprocessor;

public class PreProcessorToLowerImpl implements PreProcessor {
    @Override
    public String process(String str) {
        return str.toLowerCase();
    }
}