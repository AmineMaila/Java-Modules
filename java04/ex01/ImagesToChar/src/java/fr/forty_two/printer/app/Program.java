package fr.forty_two.printer.app;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import fr.forty_two.printer.logic.BmpPrinter;


public class Program {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.print("""
				error: arguments Invalid, correct argument order:
					1- character for white pixel
					2- character for black pixel
				""");
			return;
		}

		try {
			BufferedImage img = ImageIO.read(Program.class.getClassLoader().getResourceAsStream("resources/it.bmp"));
			if (img == null) {
				System.err.println("error: enter a valid bmp image");
				return;
			}

			if (img.getType() != BufferedImage.TYPE_BYTE_BINARY) {
				System.err.println("error: only two-colored black-and-white bmp image allowed");
				return;
			}
			var imgArray = BmpPrinter.pixelToChar(img, args[0].charAt(0), args[1].charAt(0));
			for (char[] row: imgArray) {
				for (char pixel: row) {
					System.out.print(pixel);
				}
				System.out.println();
			}
		} catch (IOException e) {
			System.err.println("error: " + e.getMessage());
		}
	}
}
