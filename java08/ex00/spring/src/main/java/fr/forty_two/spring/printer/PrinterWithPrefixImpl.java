package fr.forty_two.spring.printer;

import fr.forty_two.spring.renderer.Renderer;

public class PrinterWithPrefixImpl implements Printer {
    private final Renderer renderer;
    private String prefix = "[INFO]";


    public PrinterWithPrefixImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void print(String line) {
        renderer.render(prefix + " " + line);
    }
}
