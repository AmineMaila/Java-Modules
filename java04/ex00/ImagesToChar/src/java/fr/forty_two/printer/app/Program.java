package fr.forty_two.printer.app;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import fr.forty_two.printer.logic.BmpPrinter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;


public class Program {
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.print("""
				error: arguments Invalid, correct argument order:
					1- absolute path to the bmp file
					2- character for white pixel
					3- character for black pixel
				""");
			return;
		}

		Path bmpPath;
		try {
			bmpPath = Path.of(args[0]);
			
			if (!bmpPath.isAbsolute()) {
				System.err.println("error: enter an absolute path to a bmp image");
				return;
			}
	
			if (!Files.isRegularFile(bmpPath)) {
				System.err.println("error: path is not a file or does not exist");
				return;
			}
	
			if (args[1].length() != 1 || args[2].length() != 1) {
				System.err.println("error: enter a single character for pixel");
				return;
			}
			
			BufferedImage img = ImageIO.read(bmpPath.toFile());
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
		} catch (InvalidPathException e) {
			System.err.println("error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("error: " + e.getMessage());
		}
	}
}
