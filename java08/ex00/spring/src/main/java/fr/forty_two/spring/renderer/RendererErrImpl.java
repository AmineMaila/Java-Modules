package fr.forty_two.spring.renderer;

import fr.forty_two.spring.preprocessor.PreProcessor;

public class RendererErrImpl implements Renderer {
    private final PreProcessor preProcessor;

    public RendererErrImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    
    @Override
    public void render(String msg) {
        System.err.println(preProcessor.process(msg));
    }
}
