package fr.forty_two.printer.app;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.diogonunes.jcolor.Ansi;

import fr.forty_two.printer.logic.BmpPrinter;


public class Program {
	public static void main(String[] args) {
		Args argsObj = new Args();

		JCommander jc = JCommander.newBuilder()
			.addObject(argsObj)
			.build();

		try {
			jc.parse(args);
			BufferedImage img = ImageIO.read(Program.class.getClassLoader().getResourceAsStream("resources/it.bmp"));
			if (img == null) {
				System.err.println("error: enter a valid bmp image");
				return;
			}

			if (img.getType() != BufferedImage.TYPE_BYTE_BINARY) {
				System.err.println("error: only two-colored black-and-white bmp image allowed");
				return;
			}
			var imgArray = BmpPrinter.pixelToChar(img);
			for (char[] row: imgArray) {
				for (char pixel: row) {
					if (pixel == 'W') {
						System.out.print(Ansi.colorize(" ", argsObj.getWhite()));
					} else if (pixel == 'B') {
						System.out.print(Ansi.colorize(" ", argsObj.getBlack()));

					}
				}
				System.out.println();
			}
		} catch (IOException e) {
			System.err.println("error: " + e.getMessage());
		} catch (ParameterException e) {
			System.err.println("error: " + e.getMessage());
		}
	}
}
