package fr.forty_two.spring.renderer;

import fr.forty_two.spring.preprocessor.PreProcessor;

public class RendererStandardImpl implements Renderer {
    private final PreProcessor preProcessor;

    public RendererStandardImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    
    @Override
    public void render(String msg) {
        System.out.println(preProcessor.process(msg));
    }
}
