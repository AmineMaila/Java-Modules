package fr.forty_two.spring.printer;

import java.time.LocalDateTime;

import fr.forty_two.spring.renderer.Renderer;

public class PrinterWithDateTimeImpl implements Printer {
    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public void print(String line) {
        renderer.render(LocalDateTime.now() + " " + line);
    }
}
