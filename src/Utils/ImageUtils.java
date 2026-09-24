package Utils;

import java.awt.*;
import java.awt.image.*;

// This class has some useful image methods that are used when loading in images to the game
public class ImageUtils {
	// changes desired color to be transparent (the chosen color will not be seen in game when drawn)
	// also preserves any real transparency (alpha channel) already present in the source image,
	// so images can use either a magic transparent color, an actual alpha channel, or both
	public static BufferedImage transformColorToTransparency(BufferedImage image, Color transparentColor) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// make sure the color being matched against is treated as fully opaque, since that's how a "magic" transparent color is normally specified
		int transparentColorRGB = transparentColor.getRGB() | 0xFF000000;

		// iterates through each pixel of the image
		// if pixel matches the transparent color, that pixel becomes fully transparent
		// otherwise, the pixel (including any transparency it already has) is copied over as-is
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int argb = image.getRGB(i, j);
				if (argb == transparentColorRGB) {
					newImage.setRGB(i, j, 0x00000000);
				} else {
					newImage.setRGB(i, j, argb);
				}
			}
		}
		return newImage;
	}

	// https://stackoverflow.com/a/4216315
	// resizes an image
	public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, image.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(),
				image.getHeight(), null);
		g.dispose();
		return resized;
	}

	public static BufferedImage createSolidImage(Color color) {
		BufferedImage nothing = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		nothing = ImageUtils.transformColorToTransparency(nothing, color);
		return nothing;
	}

	public static BufferedImage createSolidImage(Color color, int width, int height) {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		image = ImageUtils.transformColorToTransparency(image, color);
		return resizeImage(image, width, height);
	}
}