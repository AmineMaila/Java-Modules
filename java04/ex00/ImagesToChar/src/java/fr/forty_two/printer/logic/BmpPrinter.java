package fr.forty_two.printer.logic;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BmpPrinter {
    public static void print(BufferedImage img, char white, char black) {
        int width = img.getWidth();
        int height = img.getHeight();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (img.getRGB(x, y) == Color.BLACK.getRGB()) {
                    System.out.print(black);
                } else if (img.getRGB(x, y) == Color.WHITE.getRGB()) {
                    System.out.print(white);
                }
            }
            System.out.println();
        }
    }

}
