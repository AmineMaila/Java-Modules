package fr.forty_two.printer.logic;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BmpPrinter {
    public static char[][] pixelToChar(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        char[][] imgArray = new char[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (img.getRGB(x, y) == Color.BLACK.getRGB()) {
                    imgArray[y][x] = 'B';
                } else if (img.getRGB(x, y) == Color.WHITE.getRGB()) {
                    imgArray[y][x] = 'W';
                }
            }
        }
        return imgArray;
    }

}
