package fr.forty_two.spring.preprocessor;

public class PreProcessorToUpperImpl implements PreProcessor {
    @Override
    public String process(String str) {
        return str.toUpperCase();
    }
}
