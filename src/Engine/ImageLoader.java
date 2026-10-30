package Engine;

import Utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// contains a bunch of helpful methods for loading images file into the game
public class ImageLoader {
    private static final Map<String, Map<Integer, BufferedImage>> imageCache = new HashMap<>();

    // loads an image and sets its transparent color to the one defined in the Config class
    public static BufferedImage load(String imageFileName) {
        return ImageLoader.load(imageFileName, Config.TRANSPARENT_COLOR);
    }

    // loads an image and allows the transparent color to be specified
    public static synchronized BufferedImage load(String imageFileName, Color transparentColor) {
        int transparentColorRGB = transparentColor.getRGB() | 0xFF000000;
        Map<Integer, BufferedImage> imagesByColor = imageCache.get(imageFileName);
        if (imagesByColor != null && imagesByColor.containsKey(transparentColorRGB)) {
            return imagesByColor.get(transparentColorRGB);
        }

        try {
            BufferedImage initialImage = ImageIO.read(new File(Config.RESOURCES_PATH + imageFileName));
            BufferedImage image = ImageUtils.transformColorToTransparency(initialImage, transparentColor);
            if (imagesByColor == null) {
                imagesByColor = new HashMap<>();
                imageCache.put(imageFileName, imagesByColor);
            }
            imagesByColor.put(transparentColorRGB, image);
            return image;
        } catch (IOException e) {
            System.out.println("Unable to find file " + Config.RESOURCES_PATH + imageFileName);
            throw new RuntimeException(e);
        }
    }

    // loads a piece of an image from an image file and sets its transparent color to the one defined in the Config class
    public static BufferedImage loadSubImage(String imageFileName, int x, int y, int width, int height) {
        return ImageLoader.loadSubImage(imageFileName, Config.TRANSPARENT_COLOR, x, y, width, height);
    }

    // loads a piece of an image from an image file and allows the transparent color to be specified
    public static BufferedImage loadSubImage(String imageFileName, Color transparentColor, int x, int y, int width, int height) {
        return ImageLoader.load(imageFileName, transparentColor).getSubimage(x, y, width, height);
    }
}
